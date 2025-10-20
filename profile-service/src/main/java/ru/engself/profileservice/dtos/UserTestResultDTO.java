package ru.engself.profileservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTestResultDTO {

    private UUID userTestResultId;

    private UserDTO userInfo;

    private LessonDTO lesson;

    private Integer score;

    private Boolean passed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
