package ru.engself.dictionaryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.dictionaryservice.dtos.DeckStatisticsDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.dtos.WordProgressDTO;
import ru.engself.dictionaryservice.enums.WordReviewResult;
import ru.engself.dictionaryservice.services.WordProgressService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/word-progress")
public class WordProgressController {

    private final WordProgressService wordProgressService;

    @PostMapping
    public ResponseEntity<WordProgressDTO> createWordProgress(@RequestBody WordDTO wordDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.createWordProgress(wordDTO, userId), HttpStatus.OK);
    }

    @GetMapping("/decks/{deckId}/statistics")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByDeckId(@PathVariable("deckId") UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getStatisticsByDeckId(deckId, userId), HttpStatus.OK);
    }

    @GetMapping("/users/statistics")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByUserId(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getStatisticsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{wordProgressId}")
    public ResponseEntity<WordProgressDTO> getWordProgressById(@PathVariable("wordProgressId") UUID wordProgressId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getWordProgressById(wordProgressId, userId), HttpStatus.OK);
    }

    @GetMapping("/words/{wordId}")
    public ResponseEntity<WordProgressDTO> getWordProgressByWordId(@PathVariable("wordId") UUID wordId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getWordProgressByWordId(wordId, userId), HttpStatus.OK);
    }

    @GetMapping("/decks/{deckId}/next")
    public ResponseEntity<List<WordProgressDTO>> getNextWords(@PathVariable UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getNextWords(deckId, userId), HttpStatus.OK);
    }

    @GetMapping("/decks/{deckId}")
    public ResponseEntity<List<WordProgressDTO>> getWordsByDeckId(@PathVariable UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getWordsByDeckId(deckId, userId), HttpStatus.OK);
    }

    @GetMapping("/users/statistics/period")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByPeriod(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(wordProgressService.getStatisticsByPeriod(startDate, endDate, userId), HttpStatus.OK);
    }

    @PostMapping("/{wordProgressId}/update")
    public ResponseEntity<Void> updateWordProgress(@PathVariable("wordProgressId") UUID wordProgressId, @RequestParam("result") WordReviewResult result, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        wordProgressService.updateWordProgress(wordProgressId, result, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}