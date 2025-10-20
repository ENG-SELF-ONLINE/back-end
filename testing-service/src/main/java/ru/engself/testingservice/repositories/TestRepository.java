package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.Test;

import java.util.Optional;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {

    Optional<Test> findTestByLessonLessonId(UUID lesson_lessonId);

}
