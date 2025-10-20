package ru.engself.activitieslibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.engself.activitieslibrary.entities.UserActivity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {

    List<UserActivity> findTop3ByUserIdOrderByActivityDateDesc(UUID userId);

    void deleteByActivityDateBefore(LocalDateTime date);
}
