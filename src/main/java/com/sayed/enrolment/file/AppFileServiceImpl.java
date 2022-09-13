package com.sayed.enrolment.file;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import com.sayed.enrolment.folder.Folder;
import com.sayed.enrolment.folder.FolderService;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppFileServiceImpl implements AppFileService {

    private final AppFileRepo fileRepo;
    private final FolderService folderService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "appfile", key = "#dto.getId()"),
            @CacheEvict(value = "appfilehistory", key = "#dto.getId()"),
            @CacheEvict(value = "folder", allEntries = true)
    })
    public void saveFile(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException {
        AppFile file = fileRepo.findById(dto.getId()).orElse(null);
        if (file == null) {
            file = new AppFile();
        }
        file.setId(dto.getId());
        file.setUrl(dto.getUrl());
        file.setDate(updateDate);
        if (file.getParent() != null) {
            folderService.deleteChildFile(file.getParent().getId(), file, updateDate);
        }
        file.setParent(dto.getParentId() == null ? null : folderService.getFolder(dto.getParentId()));
        file.setSize(dto.getSize());
        fileRepo.save(file);
        if (dto.getParentId() != null) {
            Folder folderParent = folderService.getFolder(dto.getParentId());
            folderParent.addChildFile(file);
            folderService.updateDate(folderParent.getId(), updateDate);
            folderService.saveFolder(folderParent);
        }
    }

    @Override
    @Cacheable(value = "appfile", key = "#id")
    public AppFile getFile(String id) throws AppFileNotFoundException {
        return fileRepo.findById(id).orElseThrow(
                () -> new AppFileNotFoundException("Wrong file id was provided.")
        );
    }

    @Override
    @Cacheable("appfileupdates")
    public List<AppFile> updates(Timestamp date) {
        return fileRepo.findAllByDateBetween(new Timestamp(date.getTime() - 24 * 60 * 60 * 1000), date);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "appfilehistory", key = "#id")
    public List<AppFile> getHistory(String id, Long dateStart, Long dateEnd) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(AppFile.class, false, false)
                .add(AuditEntity.id().eq(id))
                .add(AuditEntity.revisionType().eq(RevisionType.MOD));
        List<Object[]> revisions = (List<Object[]>) query.getResultList();
        List<AppFile> results = new ArrayList<>();
        for (Object[] result : revisions) {
            AppFile file = (AppFile) result[0];
            if (file.getDate().getTime() > dateStart && file.getDate().getTime() < dateEnd) {
                results.add(file);
            }
        }
        return results;
    }

    @Override
    public boolean fileDuplicateCheck(String id) {
        return fileRepo.findById(id).orElse(null) != null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "appfile", key = "#id"),
            @CacheEvict(value = "appfilehistory", key = "#id")
    })
    public void deleteFile(String id, Timestamp date) throws AppFileNotFoundException {
        AppFile file = fileRepo.findById(id).orElseThrow(
                () -> new AppFileNotFoundException("Wrong file id was provided.")
        );
        if (file.getParent() != null) {
            folderService.deleteChildFile(file.getParent().getId(), file, date);
        }
        fileRepo.delete(file);
    }
}
