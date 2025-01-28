package ru.engself.testingservice.services;

import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;

import java.util.List;
import java.util.UUID;

public interface LessonService {
    LessonDTO createLesson(LessonDTO lessonDTO, UUID userId);

    List<LessonDTO> getLessonsByLessonTypeAndLevel(Level level, LessonType type, UUID userId);

    LessonDTO getLessonById(UUID lessonId, UUID userId);

    LessonDTO updateLessonById(UUID lessonId, LessonDTO lessonDTO, UUID userId);

    String deleteLessonById(UUID lessonId, UUID userId);
}
