package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.AnswerOptionDTO;
import ru.engself.testingservice.services.AnswerOptionService;

import java.util.List;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/answer-options")
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    @PostMapping("/questions/{questionId}")
    public ResponseEntity<AnswerOptionDTO> createAnswerOption(@RequestBody AnswerOptionDTO answerOptionDTO,
                                                              @PathVariable("questionId") UUID questionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(answerOptionService.createAnswerOption(answerOptionDTO, questionId, userId), HttpStatus.OK);
    }

    @GetMapping("/{answerOptionId}")
    public ResponseEntity<AnswerOptionDTO> getAnswerOptionById(@PathVariable("answerOptionId") UUID answerOptionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(answerOptionService.getAnswerOptionById(answerOptionId, userId), HttpStatus.OK);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<List<AnswerOptionDTO>> getAnswerOptionsByQuestionId(@PathVariable("questionId") UUID questionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(answerOptionService.getAnswerOptionsByQuestionId(questionId, userId), HttpStatus.OK);
    }

    @PutMapping("/{answerOptionId}")
    public ResponseEntity<AnswerOptionDTO> updateAnswerOptionById(@PathVariable("answerOptionId") UUID answerOptionId, @RequestBody AnswerOptionDTO answerOptionDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(answerOptionService.updateAnswerOptionById(answerOptionId, answerOptionDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{answerOptionId}")
    public ResponseEntity<String> deleteAnswerOptionById(@PathVariable("answerOptionId") UUID answerOptionId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(answerOptionService.deleteAnswerOptionById(answerOptionId, userId), HttpStatus.OK);
    }

}
