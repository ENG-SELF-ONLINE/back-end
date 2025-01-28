package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.LessonMaterialDTO;
import ru.engself.testingservice.entities.LessonMaterial;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface LessonMaterialMapper {

    LessonMaterialDTO toDTO(LessonMaterial lessonMaterial);

    LessonMaterial toEntity(LessonMaterialDTO lessonMaterialDTO);

}
