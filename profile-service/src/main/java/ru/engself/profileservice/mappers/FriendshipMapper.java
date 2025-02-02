package ru.engself.profileservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.profileservice.dtos.FriendshipDTO;
import ru.engself.profileservice.entities.Friendship;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    FriendshipDTO toDTO(Friendship friendship);

    Friendship toEntity(FriendshipDTO friendshipDTO);

}
