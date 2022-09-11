package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
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
            folder.setSize(0);
        }
        folder.setId(dto.getId());
        folder.setUrl(dto.getUrl());
        folder.setDate(updateDate);
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
}
