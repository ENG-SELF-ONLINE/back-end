package ru.engself.testingservice.services;

import org.springframework.security.core.Authentication;
import ru.engself.testingservice.dtos.UserTestResultDTO;
import ru.engself.testingservice.enums.LessonType;

import java.util.List;
import java.util.UUID;

public interface UserTestResultService {
    UserTestResultDTO createUserTestResult(UserTestResultDTO userTestResultDTO, UUID lessonId, Authentication authentication);

    UserTestResultDTO getUserTestResultById(UUID userTestResultId, UUID userId);

    UserTestResultDTO updateUserTestResultById(UUID userTestResultId, UserTestResultDTO userTestResultDTO, UUID userId);

    String deleteUserTestResultById(UUID userTestResultId, UUID userId);

    UserTestResultDTO markTestAsPassed(UUID testId, UUID userId);

    UserTestResultDTO unmarkTestAsPassed(UUID testId, UUID userId);

    List<UserTestResultDTO> getAllUserTestResultsByUserId(UUID userId);

    Integer getBookProgressPercentByUserIdAndType(LessonType type, Authentication authentication);
}
