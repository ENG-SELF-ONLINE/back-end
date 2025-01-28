package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.dtos.UserDTO;
import ru.engself.testingservice.dtos.UserTestResultDTO;
import ru.engself.testingservice.entities.UserTestResult;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;
import ru.engself.testingservice.mappers.UserTestResultMapper;
import ru.engself.testingservice.repositories.UserTestResultRepository;
import ru.engself.testingservice.services.LessonService;
import ru.engself.testingservice.services.UserTestResultService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTestResultServiceImpl implements UserTestResultService {

    private final UserTestResultRepository userTestResultRepository;
    private final UserTestResultMapper userTestResultMapper;
    private final LessonService lessonService;

    @Override
    public UserTestResultDTO createUserTestResult(UserTestResultDTO userTestResultDTO, UUID lessonId, UUID userId) {

        LessonDTO lessonDTO = lessonService.getLessonById(lessonId, userId);

        UserTestResultDTO userTestResult = UserTestResultDTO.builder()
                .userInfo(
                        UserDTO.builder()
                        .userId(userId)
                        .build()
                )
                .lesson(lessonDTO)
                .score(userTestResultDTO.getScore())
                .passed(userTestResultDTO.getPassed())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userTestResultMapper.toDTO(
                userTestResultRepository.save(userTestResultMapper.toEntity(userTestResult))
        );
    }

    @Override
    public UserTestResultDTO getUserTestResultById(UUID userTestResultId, UUID userId) {
        return userTestResultRepository.findById(userTestResultId).map(userTestResultMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no UserTestResult with id: " + userTestResultId));
    }

    @Override
    public UserTestResultDTO markTestAsPassed(UUID lessonId, UUID userId) {

        UserTestResult userTestResult = userTestResultRepository.findUserTestResultByLessonLessonIdAndUserInfoUserId(lessonId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Result of lesson with id " + lessonId + " not found"));

        userTestResult.setPassed(true);
        userTestResult.setUpdatedAt(LocalDateTime.now());

        return userTestResultMapper.toDTO(userTestResultRepository.save(userTestResult));

    }

    @Override
    public UserTestResultDTO unmarkTestAsPassed(UUID lessonId, UUID userId) {

        UserTestResult userTestResult = userTestResultRepository.findUserTestResultByLessonLessonIdAndUserInfoUserId(lessonId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Result of lesson with id " + lessonId + " not found"));

        userTestResult.setPassed(false);
        userTestResult.setUpdatedAt(LocalDateTime.now());

        return userTestResultMapper.toDTO(userTestResultRepository.save(userTestResult));

    }

    @Override
    public List<UserTestResultDTO> getAllUserTestResultsByUserId(UUID userId) {
        return userTestResultRepository.findUserTestResultsByUserInfoUserId(userId).stream()
                .map(userTestResultMapper::toDTO).toList();
    }

    @Override
    public Integer getBookProgressPercentByUserIdAndType(LessonType type, UUID userId) {

        List<UserTestResultDTO> userTestResults = getAllUserTestResultsByUserId(userId);

//        UserDTO userInfo = UserDTO.builder()
//                .userId(userId)
//                .build();

//        if (userInfo == null || userInfo.getLevel() == null) {
//            return 0; // Return 0% if user level or userInfo is null
//        }

        Level userLevel = Level.B1;

        long totalBooksOnLevelAndType = userTestResults.stream()
                .map(UserTestResultDTO::getLesson)
                .filter(book -> book.getLevel() == userLevel)
                .filter(result -> result.getType() == type)
                .count();

        if (totalBooksOnLevelAndType == 0) {
            return 0; // Return 0% if there are no books on user's level
        }

        long completedBooksOnLevelAndType = userTestResults.stream()
                .filter(UserTestResultDTO::getPassed)
                .map(UserTestResultDTO::getLesson)
                .filter(book -> book.getLevel() == userLevel)
                .filter(result -> result.getType() == type)
                .count();

        return (int) Math.round((double) completedBooksOnLevelAndType / totalBooksOnLevelAndType * 100);

    }

    @Override
    public UserTestResultDTO updateUserTestResultById(UUID userTestResultId, UserTestResultDTO userTestResultDTO, UUID userId) {

        UserTestResultDTO userTestResult = getUserTestResultById(userTestResultId, userId);

        if (userTestResultDTO.getScore() != null) {
            userTestResult.setScore(userTestResultDTO.getScore());
        }

        if (userTestResultDTO.getPassed() != null) {
            userTestResult.setPassed(userTestResultDTO.getPassed());
        }
        userTestResult.setUpdatedAt(LocalDateTime.now());

        return userTestResultMapper.toDTO(
                userTestResultRepository.save(userTestResultMapper.toEntity(userTestResult))
        );
    }

    @Override
    public String deleteUserTestResultById(UUID userTestResultId, UUID userId) {

        if (userTestResultRepository.findById(userTestResultId).isEmpty()) {
            throw new EntityNotFoundException("There is no UserTestResult with id: " + userTestResultId);
        }

        userTestResultRepository.deleteById(userTestResultId);
        return "successful deleted";
    }

}
