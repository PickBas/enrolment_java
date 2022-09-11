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

import java.util.ArrayList;
import java.util.HashMap;
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

    private static ResponseEntity<?> badRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("code", String.valueOf(400));
        response.put("message", "Validation Failed");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/imports")
    public ResponseEntity<?> importData(@RequestBody ImportsRequestDto request) {
        List<String> idStorage = new ArrayList<>();
        for (EntityDto dto : request.getItems()) {
            if (!dto.validate()) {
                return badRequest();
            }
            if (idStorage.contains(dto.getId())) {
                return badRequest();
            }
            idStorage.add(dto.getId());
            if (dto.getType().equals("FOLDER") && !fileService.fileDuplicateCheck(dto.getId())) {
                try {
                    folderService.saveFolder(dto, request.getUpdateDate());
                } catch (FolderNotFoundException e) {
                    return badRequest();
                }
            } else if (dto.getType().equals("FILE") && !folderService.folderDuplicateCheck(dto.getId())) {
                try {
                    fileService.saveFile(dto, request.getUpdateDate());
                } catch (FolderNotFoundException e) {
                    return badRequest();
                }
            }
            else {
                return badRequest();
            }
        }
        return ResponseEntity.ok().build();
    }
}
