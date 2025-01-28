package ru.engself.profileservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO bookDTO);

}
