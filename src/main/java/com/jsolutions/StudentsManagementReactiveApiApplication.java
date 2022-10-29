package com.jsolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class StudentsManagementReactiveApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentsManagementReactiveApiApplication.class, args);
	}

}
