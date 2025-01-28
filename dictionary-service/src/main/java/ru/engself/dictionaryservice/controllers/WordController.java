package ru.engself.dictionaryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.services.WordService;

import java.util.Optional;
import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/words")
public class WordController {

    private final WordService wordService;

    @PostMapping(value = "/decks/{deckId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WordDTO> createWord(
            @PathVariable("deckId") UUID deckId, @RequestPart("wordDTO") WordDTO wordDTO,
            @RequestPart(value = "file", required = false) MultipartFile file, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordService.createWord(deckId, wordDTO, Optional.ofNullable(file), userId), HttpStatus.OK);
    }

    @GetMapping("/{wordId}")
    public ResponseEntity<WordDTO> getWordById(@PathVariable("wordId") UUID wordId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordService.getWordById(wordId, userId), HttpStatus.OK);
    }

    @PutMapping("/{wordId}")
    public ResponseEntity<WordDTO> updateWordById(@PathVariable("wordId") UUID wordId, @RequestBody WordDTO wordDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordService.updateWordById(wordId, wordDTO, userId), HttpStatus.OK);
    }

}
