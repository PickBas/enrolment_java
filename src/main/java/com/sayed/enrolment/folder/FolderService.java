package com.sayed.enrolment.folder;

import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

public interface FolderService {
    Folder saveFile();
    Folder getFile(String id) throws FolderNotFoundException;
    void deleteFile(String id) throws FolderNotFoundException;
}
