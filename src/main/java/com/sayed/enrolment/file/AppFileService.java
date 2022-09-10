package com.sayed.enrolment.file;

import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;

public interface AppFileService {
    AppFile saveFile();
    AppFile getFile(String id) throws AppFileNotFoundException;
    void deleteFile(String id) throws AppFileNotFoundException;
}
