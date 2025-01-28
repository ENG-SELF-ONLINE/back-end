package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.UserTestResultDTO;
import ru.engself.testingservice.entities.UserTestResult;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface UserTestResultMapper {

    UserTestResultDTO toDTO(UserTestResult userTestResult);

    UserTestResult toEntity(UserTestResultDTO userTestResultDTO);

}


