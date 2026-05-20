package com.example.Mongodb.project.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NewMongodbProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewMongodbProjectApplication.class, args);
	}

}
