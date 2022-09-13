package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.AppFile;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepo folderRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveFolder(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(dto.getId()).orElse(null);
        if (folder == null) {
            folder = new Folder();
        }
        folder.setId(dto.getId());
        folder.setUrl(dto.getUrl());
        this.updateDate(folder.getId(), updateDate);
        if (folder.getParent() != null && dto.getParentId() == null) {
            this.deleteChildFolder(folder.getParent().getId(), folder, updateDate);
        }
        folder.setParent(dto.getParentId() == null
                ? null
                : folderRepo.findById(dto.getParentId()).orElseThrow(
                        () -> new FolderNotFoundException("Could nof find folder")));
        folderRepo.save(folder);
        if (dto.getParentId() != null) {
            Folder folderParent = this.getFolder(dto.getParentId());
            this.updateDate(folderParent.getId(), updateDate);
            folderParent.addChildFolder(folder);
            folderRepo.save(folderParent);
        }
    }

    @Override
    public void saveFolder(Folder folder) {
        folderRepo.save(folder);
    }

    @CacheEvict(value = "folder", allEntries = true)
    public void updateDate(String id, Timestamp updateDate) {
        Folder folder = folderRepo.findById(id).orElse(null);
        while (folder != null) {
            folder.setDate(updateDate);
            folderRepo.save(folder);
            if (folder.getParent() == null) {
                break;
            }
            folder = folderRepo.findById(folder.getParent().getId()).orElse(null);
        }
    }

    @Override
    @Cacheable(value = "folder", key = "#id")
    public Folder getFolder(String id) throws FolderNotFoundException {
        return folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Folder> getHistory(String id, Long dateStart, Long dateEnd) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Folder.class, false, false)
                .add(AuditEntity.id().eq(id))
                .add(AuditEntity.revisionType().eq(RevisionType.MOD));
        List<Object[]> revisions = (List<Object[]>) query.getResultList();
        List<Folder> results = new ArrayList<>();
        for (Object[] result : revisions) {
            Folder folder = (Folder) result[0];
            if (folder.getDate().getTime() > dateStart && folder.getDate().getTime() < dateEnd) {
                results.add(folder);
            }
        }
        return results;
    }

    @Override
    public boolean folderDuplicateCheck(String id) {
        return folderRepo.findById(id).orElse(null) != null;
    }

    @Override
    @CacheEvict(value = "folder", allEntries = true)
    public void deleteFolder(String id, Timestamp date) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
        if (folder.getParent() != null) {
            folderRepo.findById(
                    folder.getParent().getId()
            ).ifPresent(parentFolder -> this.updateDate(parentFolder.getId(), date));
        }
        folderRepo.delete(folder);
    }

    @Override
    public void deleteChildFile(String id, AppFile file, Timestamp updateDate) {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Could not find folder")
        );
        folder.removeChildFile(file);
        this.updateDate(id, updateDate);
        folderRepo.save(folder);
    }

    @Override
    public void deleteChildFolder(String id, Folder folder, Timestamp updateDate) {
        Folder origin = folderRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Could not find folder")
        );
        origin.removeChildFolder(folder);
        this.updateDate(id, updateDate);
        folderRepo.save(origin);
    }
}
