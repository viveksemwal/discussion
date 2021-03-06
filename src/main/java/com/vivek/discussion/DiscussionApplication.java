package com.vivek.discussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DiscussionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscussionApplication.class, args);
	}

}
