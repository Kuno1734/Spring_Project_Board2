package com.sparta.Board_week2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BoardWeek2Application {

	public static void main(String[] args) {
		SpringApplication.run(BoardWeek2Application.class, args);
	}

}
