package com.example.controllers;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.services.FileService;

@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	public static final String DIRECTORY = "/Users/jameswalker/Downloads/Uploads"; //System.getenv("user.home") + "/Downloads/Uploads/";
	
	@PostMapping("/uploads")
	public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles)throws IOException{
		
		List<String> uploadedFiles = fileService.upload(multipartFiles);
		
		return ResponseEntity.ok().body(uploadedFiles);
		
	}
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException{
		
		//Resource downloadedFiles = fileService.download(filename);
		
		Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
		
		if(!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + "was not found");
		}
		
		Resource resource = new UrlResource(filePath.toUri());
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("File-Name", filename);
		
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name" + resource.getFilename());
		
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
				.headers(httpHeaders).body(resource);
		
	}
	
	
	
	

}
