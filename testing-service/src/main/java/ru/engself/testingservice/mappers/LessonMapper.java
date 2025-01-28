package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.LessonDTO;
import ru.engself.testingservice.entities.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDTO toDTO(Lesson lesson);

    Lesson toEntity(LessonDTO lessonDTO);

}
