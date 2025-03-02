package ru.engself.trackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.engself.trackerservice.repositories"})
@EntityScan(basePackages = {"ru.engself.trackerservice.entities"})
public class TrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerServiceApplication.class, args);
    }

}
