package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;
import ru.engself.testingservice.services.LessonService;

import java.util.List;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/lessons")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonDTO lessonDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonService.createLesson(lessonDTO, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessonsByLessonTypeAndLevel(Level level, LessonType type, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonService.getLessonsByLessonTypeAndLevel(level, type, userId), HttpStatus.OK);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonService.getLessonById(lessonId, userId), HttpStatus.OK);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> updateLessonById(@PathVariable("lessonId") UUID lessonId, @RequestBody LessonDTO lessonDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonService.updateLessonById(lessonId, lessonDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> deleteLessonById(@PathVariable("lessonId") UUID lessonId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonService.deleteLessonById(lessonId, userId), HttpStatus.OK);
    }

}
