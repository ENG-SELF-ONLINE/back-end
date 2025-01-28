package ru.engself.testingservice.services;

import ru.engself.testingservice.dtos.TestDTO;
import ru.engself.testingservice.dtos.create.TestCreateDTO;

import java.util.UUID;

public interface TestService {
    TestDTO createTest(TestCreateDTO testDTO, UUID lessonId, UUID userId);

    TestDTO getTestByLessonId(UUID lessonId, UUID userId);

    TestDTO getTestById(UUID testId, UUID userId);

    String deleteTestById(UUID testId, UUID userId);
}
