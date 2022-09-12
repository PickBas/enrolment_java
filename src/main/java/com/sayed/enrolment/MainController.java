package com.sayed.enrolment;

import com.sayed.enrolment.dtos.EntityDto;
import com.sayed.enrolment.dtos.ImportsRequestDto;
import com.sayed.enrolment.file.AppFileService;
import com.sayed.enrolment.file.exceptions.AppFileNotFoundException;
import com.sayed.enrolment.folder.FolderService;
import com.sayed.enrolment.folder.exceptions.FolderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

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

    @ExceptionHandler({ FolderNotFoundException.class })
    public ResponseEntity<?> handleFolderNotFoundException() {
        return badRequest();
    }

    @ExceptionHandler({ AppFileNotFoundException.class })
    public ResponseEntity<?> handleAppFileNotFoundException() {
        Map<String, String> response = new HashMap<>();
        response.put("code", String.valueOf(404));
        response.put("message", "Item not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/imports")
    public ResponseEntity<?> importData(@RequestBody ImportsRequestDto request) throws FolderNotFoundException {
        List<String> idStorage = new ArrayList<>();
        for (EntityDto dto : request.getItems()) {
            if (!dto.validate() || idStorage.contains(dto.getId())) {
                return badRequest();
            }
            idStorage.add(dto.getId());
            if (dto.getType().equals("FOLDER") && !fileService.fileDuplicateCheck(dto.getId())) {
                folderService.saveFolder(dto, request.getUpdateDate());
            } else if (dto.getType().equals("FILE") && !folderService.folderDuplicateCheck(dto.getId())) {
                fileService.saveFile(dto, request.getUpdateDate());
            }
            else {
                return badRequest();
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<?> nodeById(@PathVariable(name="id") String id) throws AppFileNotFoundException {
        try {
            return ResponseEntity.ok().body(folderService.getFolder(id));
        } catch (FolderNotFoundException e) {
            return ResponseEntity.ok().body(fileService.getFile(id));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable(name = "id") String id, @RequestParam(name = "date") @DateTimeFormat(
                    pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss.sss'Z'"}
            ) Date inputDate) throws AppFileNotFoundException {
        Timestamp date = new Timestamp(inputDate.getTime());
        try {
            folderService.deleteFolder(id, date);
            return ResponseEntity.ok().build();
        } catch (FolderNotFoundException e) {
            fileService.deleteFile(id, date);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/updates")
    public ResponseEntity<?> updates(
            @RequestParam(name = "date")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss.sss'Z'"}) Date inputDate
    ) {
        Timestamp date = new Timestamp(inputDate.getTime());
        return ResponseEntity.ok(fileService.updates(date));
    }

    @GetMapping("/node/{id}/history")
    public ResponseEntity<?> nodeHistory(
            @PathVariable String id,
            @RequestParam(name = "dateStart")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss.sss'Z'"}) Date dateStart,
            @RequestParam(name = "dateEnd")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss.sss'Z'"}) Date dateEnd) {
        return ResponseEntity.ok(fileService.getHistory(id));
    }
}
