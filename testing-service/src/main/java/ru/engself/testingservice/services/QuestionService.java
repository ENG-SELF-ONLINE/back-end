package ru.engself.testingservice.services;

import ru.engself.testingservice.dtos.QuestionDTO;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    QuestionDTO createQuestion(QuestionDTO questionDTO, UUID testId, UUID userId);

    QuestionDTO getQuestionById(UUID questionId, UUID userId);

    List<QuestionDTO> getQuestionsByTestId(UUID testId, UUID userId);

    QuestionDTO updateQuestionById(UUID questionId, QuestionDTO questionDTO, UUID userId);

    String deleteQuestionById(UUID questionId, UUID userId);
}
