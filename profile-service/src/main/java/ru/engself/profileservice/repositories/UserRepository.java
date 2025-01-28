package ru.engself.profileservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.profileservice.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
