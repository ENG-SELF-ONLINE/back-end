package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;
import ru.engself.testingservice.mappers.LessonMapper;
import ru.engself.testingservice.repositories.LessonRepository;
import ru.engself.testingservice.services.LessonService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.generateKeyPrefix;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public LessonDTO createLesson(LessonDTO lessonDTO, UUID userId) {

        LessonDTO lesson = LessonDTO.builder()
                .title(lessonDTO.getTitle())
                .level(lessonDTO.getLevel())
                .type(lessonDTO.getType())
                .lessonOrder(lessonDTO.getLessonOrder())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);
        return lessonMapper.toDTO(
                lessonRepository.save(lessonMapper.toEntity(lesson))
        );
    }

    @Override
    public List<LessonDTO> getLessonsByLessonTypeAndLevel(Level level, LessonType type, UUID userId) {
        return lessonRepository.findLessonsByTypeAndLevel(type, level).stream()
                .map(lessonMapper::toDTO)
                .sorted(Comparator.comparing(LessonDTO::getLessonOrder))
                .toList();
    }

    @Override
    public LessonDTO getLessonById(UUID lessonId, UUID userId) {
        return lessonRepository.findById(lessonId)
                .map(lessonMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no lesson with id: " + lessonId));
    }

    @Override
    public LessonDTO updateLessonById(UUID lessonId, LessonDTO lessonDTO, UUID userId) {

        LessonDTO lesson = getLessonById(lessonId, userId);

        String title = lessonDTO.getTitle();
        Level level = lessonDTO.getLevel();
        LessonType type = lessonDTO.getType();
        Integer order = lessonDTO.getLessonOrder();

        if (title != null && !title.isBlank()) {
            lesson.setTitle(title);
        }
        if (level != null) {
            lesson.setLevel(level);
        }
        if (type != null) {
            lesson.setType(type);
        }
        if (order != null) {
            lesson.setLessonOrder(order);
        }
        lesson.setUpdatedAt(LocalDateTime.now());
        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);

        return lessonMapper.toDTO(
                lessonRepository.save(lessonMapper.toEntity(lesson))
        );
    }

    @Override
    public String deleteLessonById(UUID lessonId, UUID userId) {

        if (lessonRepository.findById(lessonId).isEmpty()) {
            throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
        }

        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);
        lessonRepository.deleteById(lessonId);
        return "successful deleted";
    }
}
