package ru.engself.testingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.testingservice.entities.LessonMaterial;

import java.util.UUID;

public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, UUID> {
}
