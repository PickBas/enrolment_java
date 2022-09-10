package com.sayed.enrolment.folder;

import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepo folderRepo;

    @Override
    public Folder saveFile() {
        return null;
    }

    @Override
    public Folder getFile(String id) throws FolderNotFoundException {
        return folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
    }

    @Override
    public void deleteFile(String id) throws FolderNotFoundException {
        Folder folder = folderRepo.findById(id).orElseThrow(
                () -> new FolderNotFoundException("Wrong id was provided.")
        );
        folderRepo.delete(folder);
    }
}
