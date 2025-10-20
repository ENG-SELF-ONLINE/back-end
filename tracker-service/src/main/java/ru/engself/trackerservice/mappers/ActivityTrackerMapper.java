package ru.engself.trackerservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.trackerservice.dtos.ActivityTrackerDTO;
import ru.engself.trackerservice.entities.ActivityTracker;

@Mapper(componentModel = "spring")
public interface ActivityTrackerMapper {

    ActivityTrackerDTO toDTO(ActivityTracker entity);

    ActivityTracker toEntity(ActivityTrackerDTO dto);

}
