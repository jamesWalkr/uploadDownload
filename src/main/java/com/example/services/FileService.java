package com.example.services;

import java.nio.file.Files;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.Paths.get;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	
	//define location where file is saved
	public static final String DIRECTORY = "/Users/jameswalker/Downloads/Uploads";//System.getenv("user.home") + "/Downloads/Uploads/";
	
	// Define a method to upload files
	public List<String> upload(List<MultipartFile> multipartFiles) throws IOException{
		
		List<String> fileNames = new ArrayList<>();
		
		for(MultipartFile file : multipartFiles) {
			
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			
			Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
			
			copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
			
			fileNames.add(filename);
			
		}
		return fileNames;
		
		
	}
	
		
	//Define a method to download files
	public Resource download(String filename) throws IOException {
		
		Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
		
		if(!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + "was not found");
		}
		
		Resource resource = new UrlResource(filePath.toUri());
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("File-Name", filename);
		
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name" + resource.getFilename());
		
		return resource;
		
	}
	

}
