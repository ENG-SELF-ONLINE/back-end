package ru.engself.profileservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.profileservice.dtos.NotificationDTO;
import ru.engself.profileservice.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toDTO(Notification notification);

    Notification toEntity(NotificationDTO notificationDTO);

}
