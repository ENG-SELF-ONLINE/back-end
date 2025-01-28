package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.dtos.TestDTO;
import ru.engself.testingservice.dtos.create.AnswerOptionCreateDTO;
import ru.engself.testingservice.dtos.create.QuestionCreateDTO;
import ru.engself.testingservice.dtos.create.TestCreateDTO;
import ru.engself.testingservice.entities.AnswerOption;
import ru.engself.testingservice.entities.Question;
import ru.engself.testingservice.entities.Test;
import ru.engself.testingservice.mappers.LessonMapper;
import ru.engself.testingservice.mappers.TestMapper;
import ru.engself.testingservice.repositories.AnswerOptionRepository;
import ru.engself.testingservice.repositories.QuestionRepository;
import ru.engself.testingservice.repositories.TestRepository;
import ru.engself.testingservice.services.LessonService;
import ru.engself.testingservice.services.TestService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final LessonService lessonService;
    private final TestMapper testMapper;
    private final LessonMapper lessonMapper;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Transactional
    public TestDTO createTest(TestCreateDTO testCreateDTO, UUID lessonId, UUID userId) {
        LessonDTO lesson = lessonService.getLessonById(lessonId, userId);

        Test test = Test.builder()
                .lesson(lessonMapper.toEntity(lesson))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        test = testRepository.save(test);

        if (testCreateDTO != null) {
            for (QuestionCreateDTO questionCreateDTO : testCreateDTO.getQuestions()) {

                Question question = Question.builder()
                        .test(test)
                        .text(questionCreateDTO.getText())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                question = questionRepository.save(question);

                if (questionCreateDTO.getAnswerOptions() != null) {
                    for (AnswerOptionCreateDTO answerOptionCreateDTO : questionCreateDTO.getAnswerOptions()) {
                        AnswerOption answerOption = AnswerOption.builder()
                                .question(question)
                                .text(answerOptionCreateDTO.getText())
                                .isCorrect(answerOptionCreateDTO.getIsCorrect())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();
                        answerOptionRepository.save(answerOption);
                    }
                }
            }
        }

        return testMapper.toDTO(test);
    }

    @Override
    public TestDTO getTestByLessonId(UUID lessonId, UUID userId) {
        return testRepository.findTestByLessonLessonId(lessonId).map(testMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no test with lessonId: " + lessonId));
    }

    @Override
    public TestDTO getTestById(UUID testId, UUID userId) {
        return testRepository.findById(testId).map(testMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no test with testId: " + testId));
    }

    @Override
    public String deleteTestById(UUID testId, UUID userId) {

        if (testRepository.findById(testId).isEmpty()) {
            throw new EntityNotFoundException("There is no lesson with id: " + testId);
        }

        testRepository.deleteById(testId);
        return "successful deleted";

    }
}
