package ru.engself.testingservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.*;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.mappers.LessonMaterialMapper;
import ru.engself.testingservice.repositories.LessonMaterialRepository;
import ru.engself.testingservice.services.LessonMaterialService;
import ru.engself.testingservice.services.LessonService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonMaterialServiceImpl implements LessonMaterialService {

    private final LessonMaterialRepository lessonMaterialRepository;
    private final LessonMaterialMapper lessonMaterialMapper;
    private final LessonService lessonService;
    private final ObjectMapper objectMapper;

    @Override
    public LessonMaterialDTO createLessonMaterial(UUID lessonId, LessonMaterialDTO lessonMaterialDTO, UUID userId) {

        LessonDTO lesson = lessonService.getLessonById(lessonId, userId);

        LessonMaterialDTO lessonMaterial = LessonMaterialDTO.builder()
                .lesson(lesson)
                .content(lessonMaterialDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return lessonMaterialMapper.toDTO(
                lessonMaterialRepository.save(lessonMaterialMapper.toEntity(lessonMaterial))
        );
    }

    @Override
    public LessonMaterialDTO getLessonMaterialById(UUID lessonMaterialId, UUID userId) {
        return lessonMaterialRepository.findById(lessonMaterialId)
                .map(lessonMaterialMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no lessonMaterial with id: " + lessonMaterialId));
    }

    @Override
    public LessonDetailsDTO getLessonDetailsByLessonMaterialId(UUID lessonMaterialId, UUID userId) {

        LessonMaterialDTO lessonMaterial = getLessonMaterialById(lessonMaterialId, userId);
        LessonDTO lesson = lessonMaterial.getLesson();

        Object materials;
        try {
            if (lesson.getType() == LessonType.GRAMMAR) {
                materials = objectMapper.readValue(lessonMaterial.getContent(), GrammarContentDTO.class);
            } else {
                materials = objectMapper.readValue(lessonMaterial.getContent(), ListeningContentDTO.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON content", e);
        }

        return new LessonDetailsDTO(lesson, materials);
    }

    @Override
    public LessonMaterialDTO updateLessonMaterialById(UUID lessonMaterialId, LessonMaterialDTO lessonMaterialDTO, UUID userId) {

        LessonMaterialDTO lessonMaterial = getLessonMaterialById(lessonMaterialId, userId);

        String content = lessonMaterialDTO.getContent();

        if (content != null && !content.isBlank()) {
            lessonMaterial.setContent(content);
        }
        lessonMaterial.setUpdatedAt(LocalDateTime.now());

        return lessonMaterialMapper.toDTO(
                lessonMaterialRepository.save(lessonMaterialMapper.toEntity(lessonMaterial))
        );
    }

    @Override
    public String deleteLessonMaterialById(UUID lessonMaterialId, UUID userId) {

        if (lessonMaterialRepository.findById(lessonMaterialId).isEmpty()) {
            throw new EntityNotFoundException("There is no lessonMaterial with id: " + lessonMaterialId);
        }

        lessonMaterialRepository.deleteById(lessonMaterialId);
        return "successful deleted";
    }

}
