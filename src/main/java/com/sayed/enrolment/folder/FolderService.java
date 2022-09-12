package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.AppFile;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;

public interface FolderService {
    void saveFolder(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException;
    void saveFolder(Folder folder);
    void updateDate(String id, Timestamp updateDate);
    Folder getFolder(String id) throws FolderNotFoundException;
    boolean folderDuplicateCheck(String id);
    void deleteFolder(String id, Timestamp date) throws FolderNotFoundException;
    void deleteChildFile(String id, AppFile file, Timestamp updateDate);
    void deleteChildFolder(String id, Folder folder, Timestamp updateDate);
}
