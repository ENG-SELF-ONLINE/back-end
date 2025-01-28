package ru.engself.dictionaryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DictionaryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DictionaryServiceApplication.class, args);
	}

}
