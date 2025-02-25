package ru.engself.profileservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.engself.profileservice.entities.Friendship;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

    @Query("SELECT f FROM Friendship f WHERE f.receiver.userId = :userId OR f.sender.userId = :userId")
    Set<Friendship> findFriendshipsByUserId(UUID userId);

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.sender.userId = :senderId AND f.receiver.userId = :receiverId) " +
            "OR (f.sender.userId = :receiverId AND f.receiver.userId = :senderId)")
    Optional<Friendship> findExistingFriendship(@Param("senderId") UUID senderId, @Param("receiverId") UUID receiverId);

}
