package com.example.controllers;



import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.models.FileInfo;
import com.example.models.ResponseMessage;
import com.example.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final StorageService storageService;

    public FileUploadController(StorageService storageService){
        this.storageService = storageService;
    }



    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file){

        storageService.store(file);

        return ResponseEntity.status(HttpStatus.OK).body("file has been successfully loaded");
    }



    @GetMapping("/download/{filename:..+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){

        Resource file = storageService.loadAsResource(filename);

        if(file == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/allFiles")
    public ResponseEntity<List<FileInfo>> getListOfFiles(){
        List<FileInfo> fileList = storageService.loadAll().map(
                path -> {
                    String filename = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString()).build().toString();
                    return new FileInfo(filename, url);
                    }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileList);
    }

    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename){
        String message = "";

        try {
            boolean existed = storageService.delete(filename);
            if(existed){
                message = "Deleted the file successfully";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }
            message = "The file does not exist";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }
}



