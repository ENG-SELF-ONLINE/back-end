package ru.engself.dictionaryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.dictionaryservice.dtos.CommonWordDTO;
import ru.engself.dictionaryservice.services.CommonWordService;

import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/common-words")
public class CommonWordController {

    private final CommonWordService commonWordService;

    @GetMapping("/{word}")
    public ResponseEntity<CommonWordDTO> getOrCreateCommonWord(@PathVariable("word") String word, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(commonWordService.getOrCreateCommonWord(word, userId), HttpStatus.OK);
    }

}
