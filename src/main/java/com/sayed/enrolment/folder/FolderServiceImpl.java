package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.AppFile;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepo folderRepo;

    @Override
    public void saveFolder(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(dto.getId()).orElse(null);
        if (folder == null) {
            folder = new Folder();
        }
        folder.setId(dto.getId());
        folder.setUrl(dto.getUrl());
        this.updateDate(folder, updateDate);
        if (folder.getParent() != null && dto.getParentId() == null) {
            this.deleteChildFolder(folder.getParent().getId(), folder, updateDate);
        }
        if (dto.getParentId() != null) {
            folder.setParent(folderRepo.findById(dto.getParentId()).orElseThrow(() -> new FolderNotFoundException("Could nof find folder")));
        } else {
            folder.setParent(null);
        }
        folderRepo.save(folder);
        if (dto.getParentId() != null) {
            Folder folderParent = this.getFolder(dto.getParentId());
            folderParent.setDate(updateDate);
            folderParent.addChildFolder(folder);
            folderRepo.save(folderParent);
        }
    }

    @Override
    public void saveFolder(Folder folder) {
        folderRepo.save(folder);
    }

    @Override
    public void updateDate(Folder folder, Timestamp updateDate) {
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
    public boolean folderDuplicateCheck(String id) {
        return folderRepo.findById(id).orElse(null) != null;
    }

    @Override
    @CacheEvict(value = "folder", key="#id")
    public void deleteFolder(String id, Timestamp date) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
        if (folder.getParent() != null) {
            folderRepo.findById(folder.getParent().getId()).ifPresent(parentFolder -> this.updateDate(parentFolder, date));
        }
        folderRepo.delete(folder);
    }

    @Override
    public void deleteChildFile(String id, AppFile file, Timestamp updateDate) {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Could not find folder")
        );
        folder.removeChildFile(file);
        folder.setDate(updateDate);
        folderRepo.save(folder);
    }

    @Override
    public void deleteChildFolder(String id, Folder folder, Timestamp updateDate) {
        Folder origin = folderRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Could not find folder")
        );
        origin.removeChildFolder(folder);
        origin.setDate(updateDate);
        folderRepo.save(origin);
    }
}
