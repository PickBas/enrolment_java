package com.sayed.enrolment.file;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface AppFileService {
    void saveFile(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException;
    AppFile getFile(String id) throws AppFileNotFoundException;
    List<AppFile> updates(Timestamp date);
    List<AppFile> getHistory(String id, Long dateStart, Long dateEnd);
    boolean fileDuplicateCheck(String id);
    void deleteFile(String id, Timestamp date) throws AppFileNotFoundException;
}
