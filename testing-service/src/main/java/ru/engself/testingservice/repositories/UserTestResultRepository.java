package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.engself.testingservice.entities.UserTestResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTestResultRepository extends JpaRepository<UserTestResult, UUID> {

    @Query(value = "SELECT * FROM user_test_results WHERE lesson_id = :lessonId AND (user_info->>'userId')::uuid = :userId", nativeQuery = true)
    Optional<UserTestResult> findUserTestResultByLessonLessonIdAndUserInfoUserId(UUID lessonId, UUID userId);

    @Query(value = "SELECT * FROM user_test_results WHERE (user_info->>'userId')::uuid = :userId", nativeQuery = true)
    List<UserTestResult> findUserTestResultsByUserInfoUserId(UUID userId);

    Optional<UserTestResult> findByLessonLessonId(UUID lessonId);
}
