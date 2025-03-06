package ru.engself.testingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.engself.activitieslibrary.aspects.TrackActivity;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.dtos.UserDTO;
import ru.engself.testingservice.dtos.UserTestResultDTO;
import ru.engself.testingservice.entities.UserTestResult;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.mappers.UserTestResultMapper;
import ru.engself.testingservice.repositories.UserTestResultRepository;
import ru.engself.testingservice.services.LessonService;
import ru.engself.testingservice.services.UserTestResultService;
import ru.engself.testingservice.utils.feigns.ProfileFeignController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.engself.testingservice.utils.AuthenticationUtils.generateKeyPrefix;
import static ru.engself.testingservice.utils.AuthenticationUtils.getAuthorizationHeader;

@Service
@RequiredArgsConstructor
public class UserTestResultServiceImpl implements UserTestResultService {

    private final UserTestResultRepository userTestResultRepository;
    private final UserTestResultMapper userTestResultMapper;
    private final LessonService lessonService;
    private final ProfileFeignController profileFeignController;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public UserTestResultDTO createUserTestResult(UserTestResultDTO userTestResultDTO, UUID lessonId, Authentication authentication) {

        UserDTO user = profileFeignController.getUserById(null, getAuthorizationHeader(authentication));
        LessonDTO lessonDTO = lessonService.getLessonById(lessonId, user.getUserId());

        Optional<UserTestResult> testResult = userTestResultRepository.findUserTestResultByLessonLessonIdAndUserInfoUserId(
                lessonDTO.getLessonId(),
                user.getUserId()
        );

        if (testResult.isPresent()) {
            return userTestResultMapper.toDTO(testResult.get());
        }

        UserTestResultDTO userTestResult = UserTestResultDTO.builder()
                .userInfo(user)
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
    @TrackActivity(
            userId = "#userId",
            activityType = "#service.getActivityType(#lessonId)",
            activityTitle = "#service.getActivityTitle(#lessonId)"
    )
    public UserTestResultDTO markTestAsPassed(UUID lessonId, UUID userId) {

        UserTestResult userTestResult = userTestResultRepository.findUserTestResultByLessonLessonIdAndUserInfoUserId(lessonId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Result of lesson with id " + lessonId + " not found"));

        userTestResult.setPassed(true);
        userTestResult.setUpdatedAt(LocalDateTime.now());
        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);

        return userTestResultMapper.toDTO(userTestResultRepository.save(userTestResult));

    }

    @Override
    public UserTestResultDTO unmarkTestAsPassed(UUID lessonId, UUID userId) {

        UserTestResult userTestResult = userTestResultRepository.findUserTestResultByLessonLessonIdAndUserInfoUserId(lessonId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Result of lesson with id " + lessonId + " not found"));

        userTestResult.setPassed(false);
        userTestResult.setUpdatedAt(LocalDateTime.now());
        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);

        return userTestResultMapper.toDTO(userTestResultRepository.save(userTestResult));

    }

    @Override
    public List<UserTestResultDTO> getAllUserTestResultsByUserId(UUID userId) {
        return userTestResultRepository.findUserTestResultsByUserInfoUserId(userId).stream()
                .map(userTestResultMapper::toDTO).toList();
    }

    @Override
    public Integer getBookProgressPercentByUserIdAndType(UUID userId, LessonType type, Authentication authentication) {

        UserDTO user = profileFeignController.getUserById(userId, getAuthorizationHeader(authentication));
        List<UserTestResultDTO> userTestResults = getAllUserTestResultsByUserId(user.getUserId());

        if (user.getLevel() == null) {
            return 0;
        }

        long totalBooksOnLevelAndType = userTestResults.stream()
                .map(UserTestResultDTO::getLesson)
                .filter(book -> book.getLevel() == user.getLevel())
                .filter(result -> result.getType() == type)
                .count();

        if (totalBooksOnLevelAndType == 0) {
            return 0;
        }

        long completedBooksOnLevelAndType = userTestResults.stream()
                .filter(UserTestResultDTO::getPassed)
                .map(UserTestResultDTO::getLesson)
                .filter(book -> book.getLevel() == user.getLevel())
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
        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);

        return userTestResultMapper.toDTO(
                userTestResultRepository.save(userTestResultMapper.toEntity(userTestResult))
        );
    }

    @Override
    public String deleteUserTestResultById(UUID userTestResultId, UUID userId) {

        if (userTestResultRepository.findById(userTestResultId).isEmpty()) {
            throw new EntityNotFoundException("There is no UserTestResult with id: " + userTestResultId);
        }

        String keyPrefix = generateKeyPrefix("test_progress_type", userId);
        kafkaTemplate.send("testing-updates", keyPrefix);
        userTestResultRepository.deleteById(userTestResultId);
        return "successful deleted";
    }

    public String getActivityType(UUID lessonId) {
        UserTestResultDTO resultDTO = getUserTestResultDTO(lessonId);
        return resultDTO.getLesson().getType().toString();
    }

    public String getActivityTitle(UUID lessonId) {
        UserTestResultDTO resultDTO = getUserTestResultDTO(lessonId);
        return resultDTO.getLesson().getTitle();
    }

    private UserTestResultDTO getUserTestResultDTO(UUID lessonId) {
        UserTestResult userTestResult = userTestResultRepository
                .findFirstByLessonLessonIdOrderByCreatedAtDesc(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Result of lesson with id " + lessonId + " not found"));

        return userTestResultMapper.toDTO(userTestResult);
    }

}
