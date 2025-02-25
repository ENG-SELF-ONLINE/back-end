package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.UserTestResultDTO;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.services.UserTestResultService;

import java.util.List;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user-test-results")
public class UserTestResultController {

    private final UserTestResultService userTestResultService;

    @PostMapping("/lessons/{lessonId}")
    public ResponseEntity<UserTestResultDTO> createUserTestResult(@RequestBody UserTestResultDTO userTestResultDTO,
                                                                  @PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        return new ResponseEntity<>(userTestResultService.createUserTestResult(userTestResultDTO, lessonId, authentication), HttpStatus.OK);
    }

    @PostMapping("/lessons/{lessonId}/mark-passed")
    public ResponseEntity<UserTestResultDTO> markTestAsPassed(@PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.markTestAsPassed(lessonId, userId), HttpStatus.OK);
    }

    @PostMapping("/lessons/{lessonId}/unmark-passed")
    public ResponseEntity<UserTestResultDTO> unmarkTestAsPassed(@PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.unmarkTestAsPassed(lessonId, userId), HttpStatus.OK);
    }

    @GetMapping("/{userTestResultId}")
    public ResponseEntity<UserTestResultDTO> getUserTestResultById(@PathVariable("userTestResultId") UUID userTestResultId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.getUserTestResultById(userTestResultId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserTestResultDTO>> getAllUserTestResultsByUserId(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.getAllUserTestResultsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/percent")
    public ResponseEntity<Integer> getBookProgressPercentByUserIdAndType(@RequestParam(value = "userId", required = false) UUID userId, LessonType type, Authentication authentication) {
        return new ResponseEntity<>(userTestResultService.getBookProgressPercentByUserIdAndType(userId, type, authentication), HttpStatus.OK);
    }

    @PutMapping("/{userTestResultId}")
    public ResponseEntity<UserTestResultDTO> updateUserTestResult(@PathVariable("userTestResultId") UUID userTestResultId,
                                                                  @RequestBody UserTestResultDTO userTestResultDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.updateUserTestResultById(userTestResultId, userTestResultDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userTestResultId}")
    public ResponseEntity<String> deleteUserTestResultById(@PathVariable("userTestResultId") UUID userTestResultId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userTestResultService.deleteUserTestResultById(userTestResultId, userId), HttpStatus.OK);
    }

}
