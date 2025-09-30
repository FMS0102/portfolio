package com.fms.backend.mappers;

import com.fms.backend.dto.UserDTO;
import com.fms.backend.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    List<UserDTO> toDTOList(List<User> userList);
}
