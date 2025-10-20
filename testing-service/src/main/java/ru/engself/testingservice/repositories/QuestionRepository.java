package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findQuestionsByTestTestId(UUID test_testId);

}
