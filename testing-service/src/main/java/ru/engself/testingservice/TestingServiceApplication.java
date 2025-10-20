package ru.engself.testingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.engself.testingservice.repositories"})
@EntityScan(basePackages = {"ru.engself.testingservice.entities"})
public class TestingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingServiceApplication.class, args);
    }

}
