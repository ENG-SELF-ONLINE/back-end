package ru.engself.readingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.BookProgressDTO;
import ru.engself.readingservice.services.BookProgressService;

import java.util.List;
import java.util.UUID;

import static ru.engself.readingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/book-progress")
public class BookProgressController {

    private final BookProgressService bookProgressService;

    @GetMapping
    public ResponseEntity<List<BookProgressDTO>> getAllBookProgressesByUserId(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookProgressService.getAllBookProgressesByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/percent")
    public ResponseEntity<Integer> getBookProgressPercentByUserId(Authentication authentication) {
        return new ResponseEntity<>(bookProgressService.getBookProgressPercentByUserId(authentication), HttpStatus.OK);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookProgressDTO> getBookProgressByBookId(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        return new ResponseEntity<>(bookProgressService.getBookProgressByBookId(bookId, authentication), HttpStatus.OK);
    }

    @PostMapping("/{bookId}/mark-completed")
    public ResponseEntity<BookProgressDTO> markBookAsCompleted(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookProgressService.markBookAsCompleted(bookId, userId), HttpStatus.OK);
    }

    @PostMapping("/{bookId}/unmark-completed")
    public ResponseEntity<BookProgressDTO> unmarkBookAsCompleted(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookProgressService.unmarkBookAsCompleted(bookId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookProgressDTO> createBookProgress(@RequestBody BookDTO bookDTO, Authentication authentication) {
        return new ResponseEntity<>(bookProgressService.createBookProgress(bookDTO, authentication), HttpStatus.OK);
    }

}
