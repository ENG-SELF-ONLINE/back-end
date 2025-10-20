package ru.engself.activitieslibrary.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.engself.activitieslibrary.aspects.ActivityAspect;
import ru.engself.activitieslibrary.mappers.UserActivityMapper;
import ru.engself.activitieslibrary.repositories.UserActivityRepository;
import ru.engself.activitieslibrary.services.UserActivityService;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"ru.engself.activitieslibrary"})
@EnableJpaRepositories(basePackages = {"ru.engself.activitieslibrary.repositories"})
@EntityScan(basePackages = {"ru.engself.activitieslibrary.entities"})
public class UserActivityConfig {

    @Bean
    public ActivityAspect activityAspect(UserActivityService userActivityService) {
        return new ActivityAspect(userActivityService);
    }

    @Bean
    public UserActivityService userActivityService(UserActivityRepository userActivityRepository, UserActivityMapper userActivityMapper) {
        return new UserActivityService(userActivityRepository, userActivityMapper);
    }
}
