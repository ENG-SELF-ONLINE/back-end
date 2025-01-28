package ru.engself.testingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.testingservice.dtos.LessonDetailsDTO;
import ru.engself.testingservice.dtos.LessonMaterialDTO;
import ru.engself.testingservice.services.LessonMaterialService;

import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/lesson-materials")
public class LessonMaterialController {

    private final LessonMaterialService lessonMaterialService;

    @PostMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonMaterialDTO> createLessonMaterial(@PathVariable("lessonId") UUID lessonId,
                                                                  @RequestBody LessonMaterialDTO lessonMaterialDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonMaterialService.createLessonMaterial(lessonId, lessonMaterialDTO, userId), HttpStatus.OK);
    }

    @GetMapping("/{lessonMaterialId}")
    public ResponseEntity<LessonMaterialDTO> getLessonMaterialById(@PathVariable("lessonMaterialId") UUID lessonMaterialId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonMaterialService.getLessonMaterialById(lessonMaterialId, userId), HttpStatus.OK);
    }

    @GetMapping("/{lessonMaterialId}/details")
    public ResponseEntity<LessonDetailsDTO> getLessonDetailsByLessonMaterialId(@PathVariable("lessonMaterialId") UUID lessonMaterialId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonMaterialService.getLessonDetailsByLessonMaterialId(lessonMaterialId, userId), HttpStatus.OK);
    }

    @PutMapping("/{lessonMaterialId}")
    public ResponseEntity<LessonMaterialDTO> updateLessonMaterialById(@PathVariable("lessonMaterialId") UUID lessonMaterialId,
                                                                      @RequestBody LessonMaterialDTO lessonMaterialDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonMaterialService.updateLessonMaterialById(lessonMaterialId, lessonMaterialDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{lessonMaterialId}")
    public ResponseEntity<String> deleteLessonMaterialById(@PathVariable("lessonMaterialId") UUID lessonMaterialId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(lessonMaterialService.deleteLessonMaterialById(lessonMaterialId, userId), HttpStatus.OK);
    }

}
