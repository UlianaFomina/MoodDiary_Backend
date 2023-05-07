package com.mood.diary.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "MoodDiary API",
				description = "MoodDiary place where you can tell about your feelings / day and collect statistics",
				version = "1.0.0",
				contact = @Contact(
						name = "Fomina Uliana",
						email = "6758599@gmail.com",
						url = "https://t.me/uliana_fomina"
				)
		)
)
public class MoodDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodDiaryApplication.class, args);
	}
}
