package ru.engself.dictionaryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.services.DeckService;

import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/decks")
public class DeckController {

    private final DeckService deckService;

    @PostMapping
    public ResponseEntity<DeckDTO> createDeck(@RequestParam String name, @RequestParam("file") MultipartFile file, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(deckService.createDeck(name, file, userId), HttpStatus.OK);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DeckDTO> getDeckById(@PathVariable("deckId") UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(deckService.getDeckById(deckId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<DeckDTO>> getAllDecksByUserId(@PageableDefault(size = 15) Pageable pageable, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(deckService.getAllDecksByUserId(userId, pageable), HttpStatus.OK);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<DeckDTO> updateDeckById(@PathVariable("deckId") UUID deckId, @RequestBody DeckDTO deckDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(deckService.updateDeckById(deckId, deckDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<String> deleteDeckById(@PathVariable("deckId") UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(deckService.deleteDeckById(deckId, userId), HttpStatus.OK);
    }

}