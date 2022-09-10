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
    public void saveFolder(EntityDto dto, Timestamp updateDate) {
    }

    @Override
    public Folder getFolder(String id) throws FolderNotFoundException {
        return folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
    }

    @Override
    public void deleteFolder(String id) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
        folderRepo.delete(folder);
    }
}
