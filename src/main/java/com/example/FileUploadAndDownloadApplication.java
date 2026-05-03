package com.example;

import com.example.storage.StorageProperties;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileUploadAndDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadAndDownloadApplication.class, args);
	}

}
