package com.ndinhchien.langPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LangPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LangPracticeApplication.class, args);
	}

}
