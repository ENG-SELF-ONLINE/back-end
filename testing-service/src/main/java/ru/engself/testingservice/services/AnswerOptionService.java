package ru.engself.testingservice.services;

import ru.engself.testingservice.dtos.AnswerOptionDTO;

import java.util.List;
import java.util.UUID;

public interface AnswerOptionService {
    AnswerOptionDTO createAnswerOption(AnswerOptionDTO answerOptionDTO, UUID questionId, UUID userId);

    AnswerOptionDTO getAnswerOptionById(UUID answerOptionId, UUID userId);

    List<AnswerOptionDTO> getAnswerOptionsByQuestionId(UUID questionId, UUID userId);

    AnswerOptionDTO updateAnswerOptionById(UUID answerOptionId, AnswerOptionDTO answerOptionDTO, UUID userId);

    String deleteAnswerOptionById(UUID answerOptionId, UUID userId);
}
