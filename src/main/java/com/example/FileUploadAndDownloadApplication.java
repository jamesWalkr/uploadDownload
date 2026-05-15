package com.example;

import com.example.services.FileSystemStorageService;
import com.example.storage.StorageProperties;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileUploadAndDownloadApplication implements CommandLineRunner {
	@Resource
	FileSystemStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(FileUploadAndDownloadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}
}
