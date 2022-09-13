package com.sayed.enrolment.folder;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.file.AppFile;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface FolderService {
    void saveFolder(EntityDto dto, Timestamp updateDate) throws FolderNotFoundException;
    void saveFolder(Folder folder);
    void updateDate(String id, Timestamp updateDate);
    Folder getFolder(String id) throws FolderNotFoundException;
    List<Folder> getHistory(String id, Long dateStart, Long dateEnd);
    boolean folderDuplicateCheck(String id);
    void deleteFolder(String id, Timestamp date) throws FolderNotFoundException;
    void deleteChildFile(String id, AppFile file, Timestamp updateDate);
    void deleteChildFolder(String id, Folder folder, Timestamp updateDate);
}
