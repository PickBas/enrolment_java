package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;

public interface FolderService {
    void saveFolder(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException;
    void saveFolder(Folder folder);
    Folder getFolder(String id) throws FolderNotFoundException;
    boolean folderDuplicateCheck(String id);
    void deleteFolder(String id) throws FolderNotFoundException;
}
