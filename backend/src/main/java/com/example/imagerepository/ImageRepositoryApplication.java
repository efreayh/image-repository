package com.example.imagerepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ImageRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageRepositoryApplication.class, args);
	}

}
