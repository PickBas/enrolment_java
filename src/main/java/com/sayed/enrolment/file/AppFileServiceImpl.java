package com.sayed.enrolment.file;

import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppFileServiceImpl implements AppFileService {

    private final AppFileRepo fileRepo;

    @Override
    public AppFile saveFile() {
        return null;
    }

    @Override
    public AppFile getFile(String id) throws AppFileNotFoundException {
        return fileRepo.findById(id).orElseThrow(() -> new AppFileNotFoundException("Wrong file id was provided."));
    }

    @Override
    public void deleteFile(String id) throws AppFileNotFoundException {
        AppFile file = fileRepo.findById(id).orElseThrow(
                () -> new AppFileNotFoundException("Wrong file id was provided.")
        );
        fileRepo.delete(file);
    }
}
