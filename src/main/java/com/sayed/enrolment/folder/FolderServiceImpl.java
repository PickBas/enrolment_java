package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.AppFile;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
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
        if (folder.getParentId() != null && dto.getParentId() == null) {
            this.deleteChildFolder(folder.getParentId(), folder, updateDate);
        }
        folder.setParentId(dto.getParentId());
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
            if (folder.getParentId() == null) {
                break;
            }
            folder = folderRepo.findById(folder.getParentId()).orElse(null);
        }
    }

    @Override
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
    public void deleteFolder(String id) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
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
