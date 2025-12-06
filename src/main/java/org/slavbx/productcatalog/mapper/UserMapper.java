package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.UserDto;
import org.slavbx.productcatalog.model.User;

import java.util.List;

/**
 * Маппер для преобразования между сущностью User и DTO
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);

    List<UserDto> usersToUserDTOs(List<User> users);

    User userDTOToUser(UserDto userDTO);
}
