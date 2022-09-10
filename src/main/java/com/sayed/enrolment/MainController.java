package com.sayed.enrolment;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.dtos.ImportsRequestDto;
import com.sayed.enrolment.file.AppFileService;
import com.sayed.enrolment.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        return ResponseEntity.ok().build();
    }
}
