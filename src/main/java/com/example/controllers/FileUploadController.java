package com.example.controllers;



import java.io.IOException;
import com.example.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



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


}
