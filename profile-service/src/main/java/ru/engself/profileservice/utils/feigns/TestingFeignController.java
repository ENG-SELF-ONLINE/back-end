package ru.engself.profileservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.engself.profileservice.dtos.LessonDTO;
import ru.engself.profileservice.dtos.UserTestResultDTO;
import ru.engself.profileservice.enums.LessonType;
import ru.engself.profileservice.enums.Level;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "testing-service", url = "${other.service.url.testing}")
public interface TestingFeignController {

    @GetMapping("/lessons")
    List<LessonDTO> getLessonsByLessonTypeAndLevel(@RequestParam("level") Level level,
                                                   @RequestParam("type") LessonType type,
                                                   @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/user-test-results/lessons/{lessonId}")
    UserTestResultDTO createUserTestResult(@RequestBody UserTestResultDTO userTestResultDTO,
                                                           @PathVariable("lessonId") UUID lessonId,
                                                           @RequestHeader("Authorization") String authorizationHeader);

}
