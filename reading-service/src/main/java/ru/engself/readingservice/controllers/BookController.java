package ru.engself.readingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.enums.Level;
import ru.engself.readingservice.services.BookService;

import java.util.List;
import java.util.UUID;

import static ru.engself.readingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestPart("bookDTO") BookDTO bookDTO, @RequestPart("book") MultipartFile book,
                                              @RequestPart("image") MultipartFile image, Authentication authentication) {
        return new ResponseEntity<>(bookService.createBook(bookDTO, book, image, authentication), HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.getBookById(bookId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllByLevel(@PageableDefault Pageable pageable, Level level, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.getAllByLevel(level, userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/without-page")
    public ResponseEntity<List<BookDTO>> getAllByLevelWithoutPage(Level level, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.getAllByLevelWithoutPage(level, userId), HttpStatus.OK);
    }

    @GetMapping("/{bookId}/download")
    public ResponseEntity<ByteArrayResource> downloadBook(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.downloadBook(bookId, userId), HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable("bookId") UUID bookId, @RequestBody BookDTO bookDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.updateBookById(bookId, bookDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBookById(@PathVariable("bookId") UUID bookId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(bookService.deleteBookById(bookId, userId), HttpStatus.OK);
    }

}