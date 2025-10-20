package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.AnswerOptionDTO;
import ru.engself.testingservice.dtos.QuestionDTO;
import ru.engself.testingservice.mappers.AnswerOptionMapper;
import ru.engself.testingservice.repositories.AnswerOptionRepository;
import ru.engself.testingservice.services.AnswerOptionService;
import ru.engself.testingservice.services.QuestionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerOptionServiceImpl implements AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final AnswerOptionMapper answerOptionMapper;
    private final QuestionService questionService;

    @Override
    public AnswerOptionDTO createAnswerOption(AnswerOptionDTO answerOptionDTO, UUID questionId, UUID userId) {

        QuestionDTO question = questionService.getQuestionById(questionId, userId);

        AnswerOptionDTO answerOption = AnswerOptionDTO.builder()
                .question(question)
                .text(answerOptionDTO.getText())
                .isCorrect(answerOptionDTO.getIsCorrect())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return answerOptionMapper.toDTO(
                answerOptionRepository.save(answerOptionMapper.toEntity(answerOption))
        );
    }

    @Override
    public AnswerOptionDTO getAnswerOptionById(UUID answerOptionId, UUID userId) {
        return answerOptionRepository.findById(answerOptionId).map(answerOptionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no answerOption with id: " + answerOptionId));
    }

    @Override
    public List<AnswerOptionDTO> getAnswerOptionsByQuestionId(UUID questionId, UUID userId) {
        return answerOptionRepository.findAnswerOptionsByQuestionQuestionId(questionId).stream()
                .map(answerOptionMapper::toDTO).toList();
    }

    @Override
    public AnswerOptionDTO updateAnswerOptionById(UUID answerOptionId, AnswerOptionDTO answerOptionDTO, UUID userId) {

        AnswerOptionDTO answerOption = getAnswerOptionById(answerOptionId, userId);

        String text = answerOptionDTO.getText();
        Boolean isCorrect = answerOptionDTO.getIsCorrect();

        if (text != null && !text.isBlank()) {
            answerOption.setText(text);
        }
        if (isCorrect != null) {
            answerOption.setIsCorrect(isCorrect);
        }
        answerOption.setUpdatedAt(LocalDateTime.now());

        return answerOptionMapper.toDTO(
                answerOptionRepository.save(answerOptionMapper.toEntity(answerOption))
        );
    }

    @Override
    public String deleteAnswerOptionById(UUID answerOptionId, UUID userId) {

        if (answerOptionRepository.findById(answerOptionId).isEmpty()) {
            throw new EntityNotFoundException("There is no answerOption with id: " + answerOptionId);
        }

        answerOptionRepository.deleteById(answerOptionId);
        return "successful deleted";
    }
}
