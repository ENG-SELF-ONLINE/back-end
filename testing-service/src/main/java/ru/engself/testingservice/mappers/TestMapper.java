package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.TestDTO;
import ru.engself.testingservice.entities.Test;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface TestMapper {

    TestDTO toDTO(Test test);

    Test toEntity(TestDTO testDTO);

}
