package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.TestDTO;
import ru.engself.testingservice.dtos.create.TestCreateDTO;
import ru.engself.testingservice.services.TestService;

import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/tests")
public class TestController {

    private final TestService testService;

    @PostMapping("/lessons/{lessonId}")
    public ResponseEntity<TestDTO> createTest(@RequestBody TestCreateDTO testCreateDTO, @PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(testService.createTest(testCreateDTO, lessonId, userId), HttpStatus.OK);
    }

    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<TestDTO> getTestByLessonId(@PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(testService.getTestByLessonId(lessonId, userId), HttpStatus.OK);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<TestDTO> getTestById(@PathVariable("testId") UUID testId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(testService.getTestById(testId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<String> deleteTestById(@PathVariable("testId") UUID testId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(testService.deleteTestById(testId, userId), HttpStatus.OK);
    }

}
