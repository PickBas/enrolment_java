package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;

public interface FolderService {
    void saveFolder(EntityDto dto, Timestamp updateDate);
    Folder getFolder(String id) throws FolderNotFoundException;
    void deleteFolder(String id) throws FolderNotFoundException;
}
