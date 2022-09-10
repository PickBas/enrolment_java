package com.sayed.enrolment;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.dtos.ImportsRequestDto;
import com.sayed.enrolment.file.AppFileService;
import com.sayed.enrolment.folder.FolderService;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MainController {

    private final FolderService folderService;
    private final AppFileService fileService;

    @Autowired
    public MainController(FolderService folder, AppFileService file) {
        this.folderService = folder;
        this.fileService = file;
    }

    @PostMapping("/imports")
    public ResponseEntity<?> importData(@RequestBody ImportsRequestDto request) {
        for (EntityDto dto : request.getItems()) {
            if (dto.getType().equals("FOLDER")) {
                folderService.saveFolder(dto, request.getUpdateDate());
            } else if (dto.getType().equals("FILE")) {
                try {
                    fileService.saveFile(dto, request.getUpdateDate());
                } catch (FolderNotFoundException e) {
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        return ResponseEntity.ok().build();
    }
}
