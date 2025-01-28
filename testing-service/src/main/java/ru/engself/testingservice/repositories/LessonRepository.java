package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.Lesson;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findLessonsByTypeAndLevel(LessonType type, Level level);

}
