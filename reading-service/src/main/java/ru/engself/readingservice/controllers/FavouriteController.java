package ru.engself.readingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.readingservice.dtos.FavouriteDTO;
import ru.engself.readingservice.services.FavouriteService;

import java.util.UUID;

import static ru.engself.readingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;

    @GetMapping
    public ResponseEntity<Page<FavouriteDTO>> getAllByUserId(@PageableDefault Pageable pageable, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(favouriteService.getAllByUserId(userId, pageable), HttpStatus.OK);
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<FavouriteDTO> addToFavourites(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(favouriteService.addToFavourites(userId, bookId), HttpStatus.OK);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<String> removeFromFavourites(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(favouriteService.removeFromFavourites(userId, bookId), HttpStatus.OK);
    }

}
