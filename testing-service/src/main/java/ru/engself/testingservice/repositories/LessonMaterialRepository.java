package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.LessonMaterial;

import java.util.Optional;
import java.util.UUID;

public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, UUID> {

    Optional<LessonMaterial> findLessonMaterialByLessonLessonId(UUID lesson_lessonId);

}
