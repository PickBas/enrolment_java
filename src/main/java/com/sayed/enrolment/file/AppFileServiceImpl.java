package com.sayed.enrolment.file;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import com.sayed.enrolment.folder.Folder;
import com.sayed.enrolment.folder.FolderService;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AppFileServiceImpl implements AppFileService {

    private final AppFileRepo fileRepo;
    private final FolderService folderService;

    @Override
    public void saveFile(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException {
        AppFile file = fileRepo.findById(dto.getId()).orElse(null);
        if (file == null) {
            file = new AppFile();
        }
        file.setId(dto.getId());
        file.setUrl(dto.getUrl());
        file.setDate(updateDate);
        if (file.getParentId() != null && dto.getParentId() == null) {
            folderService.deleteChildFile(file.getParentId(), file, updateDate);
        }
        file.setParentId(dto.getParentId());
        file.setSize(dto.getSize());
        fileRepo.save(file);
        if (dto.getParentId() != null) {
            Folder folder = folderService.getFolder(dto.getParentId());
            folderService.updateDate(folder, updateDate);
            folder.addChildFile(file);
            folderService.saveFolder(folder);
        }
    }

    @Override
    public AppFile getFile(String id) throws AppFileNotFoundException {
        return fileRepo.findById(id).orElseThrow(
                () -> new AppFileNotFoundException("Wrong file id was provided.")
        );
    }

    @Override
    public boolean fileDuplicateCheck(String id) {
        return fileRepo.findById(id).orElse(null) != null;
    }

    @Override
    public void deleteFile(String id) throws AppFileNotFoundException {
        AppFile file = fileRepo.findById(id).orElseThrow(
                () -> new AppFileNotFoundException("Wrong file id was provided.")
        );
        fileRepo.delete(file);
    }
}
