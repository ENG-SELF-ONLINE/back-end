package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.QuestionDTO;
import ru.engself.testingservice.dtos.TestDTO;
import ru.engself.testingservice.mappers.QuestionMapper;
import ru.engself.testingservice.repositories.QuestionRepository;
import ru.engself.testingservice.services.QuestionService;
import ru.engself.testingservice.services.TestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final TestService testService;

    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDTO, UUID testId, UUID userId) {

        TestDTO test = testService.getTestById(testId, userId);

        QuestionDTO question = QuestionDTO.builder()
                .test(test)
                .text(questionDTO.getText())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionMapper.toDTO(
                questionRepository.save(questionMapper.toEntity(question))
        );
    }

    @Override
    public QuestionDTO getQuestionById(UUID questionId, UUID userId) {
        return questionRepository.findById(questionId).map(questionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no question with questionId: " + questionId));
    }

    @Override
    public List<QuestionDTO> getQuestionsByTestId(UUID testId, UUID userId) {
        return questionRepository.findQuestionsByTestTestId(testId).stream()
                .map(questionMapper::toDTO).toList();
    }

    @Override
    public QuestionDTO updateQuestionById(UUID questionId, QuestionDTO questionDTO, UUID userId) {

        QuestionDTO question = getQuestionById(questionId, userId);

        String text = questionDTO.getText();

        if (text != null && !text.isBlank()) {
            question.setText(text);
        }
        question.setUpdatedAt(LocalDateTime.now());

        return questionMapper.toDTO(
                questionRepository.save(questionMapper.toEntity(question))
        );
    }

    @Override
    public String deleteQuestionById(UUID questionId, UUID userId) {

        if (questionRepository.findById(questionId).isEmpty()) {
            throw new EntityNotFoundException("There is no question with id: " + questionId);
        }

        questionRepository.deleteById(questionId);
        return "successful deleted";
    }
}
