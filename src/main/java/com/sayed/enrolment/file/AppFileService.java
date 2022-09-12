package com.sayed.enrolment.file;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;

public interface AppFileService {
    void saveFile(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException;
    AppFile getFile(String id) throws AppFileNotFoundException;
    boolean fileDuplicateCheck(String id);
    void deleteFile(String id, Timestamp date) throws AppFileNotFoundException;
}
