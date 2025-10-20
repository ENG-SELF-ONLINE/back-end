package ru.engself.activitieslibrary.mappers;

import org.mapstruct.Mapper;
import ru.engself.activitieslibrary.dtos.UserActivityDTO;
import ru.engself.activitieslibrary.entities.UserActivity;

@Mapper(componentModel = "spring")
public interface UserActivityMapper {
    UserActivityDTO toDto(UserActivity userActivity);
    UserActivity toEntity(UserActivityDTO userActivityDTO);
}
