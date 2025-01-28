package ru.engself.testingservice.services;

import ru.engself.testingservice.dtos.LessonDetailsDTO;
import ru.engself.testingservice.dtos.LessonMaterialDTO;

import java.util.UUID;

public interface LessonMaterialService {
    LessonMaterialDTO createLessonMaterial(UUID lessonId, LessonMaterialDTO lessonMaterialDTO, UUID userId);

    LessonMaterialDTO getLessonMaterialById(UUID lessonMaterialId, UUID userId);

    LessonDetailsDTO getLessonDetailsByLessonMaterialId(UUID lessonMaterialId, UUID userId);

    String deleteLessonMaterialById(UUID lessonMaterialId, UUID userId);

    LessonMaterialDTO updateLessonMaterialById(UUID lessonMaterialId, LessonMaterialDTO lessonMaterialDTO, UUID userId);
}
