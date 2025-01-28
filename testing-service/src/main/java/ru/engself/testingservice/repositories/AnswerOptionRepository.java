package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.AnswerOption;

import java.util.List;
import java.util.UUID;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, UUID> {

    List<AnswerOption> findAnswerOptionsByQuestionQuestionId(UUID question_questionId);

}
