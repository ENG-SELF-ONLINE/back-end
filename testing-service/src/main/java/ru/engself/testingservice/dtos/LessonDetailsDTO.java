package ru.engself.testingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LessonDetailsDTO {

    private LessonDTO lesson;

    private Object materials;

}
