package ru.engself.profileservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.engself.profileservice.entities.Friendship;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

    @Query("SELECT f FROM Friendship f WHERE f.receiver.userId = :userId OR f.sender.userId = :userId")
    List<Friendship> findFriendshipsByUserId(UUID userId);

}
