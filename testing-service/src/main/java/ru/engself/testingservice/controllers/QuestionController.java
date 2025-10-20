package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.QuestionDTO;
import ru.engself.testingservice.services.QuestionService;

import java.util.List;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/tests/{testId}")
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable("testId") UUID testId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(questionService.createQuestion(questionDTO, testId, userId), HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable("questionId") UUID questionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(questionService.getQuestionById(questionId, userId), HttpStatus.OK);
    }

    @GetMapping("/tests/{testId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTestId(@PathVariable("testId") UUID testId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(questionService.getQuestionsByTestId(testId, userId), HttpStatus.OK);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestionById(@PathVariable("questionId") UUID questionId, @RequestBody QuestionDTO questionDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(questionService.updateQuestionById(questionId, questionDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable("questionId") UUID questionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(questionService.deleteQuestionById(questionId, userId), HttpStatus.OK);
    }

}
