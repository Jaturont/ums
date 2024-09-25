package org.example.usermanagement.mapper;

import org.example.usermanagement.dto.UserDTO;
import org.example.usermanagement.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(UserEntity userEntity);

    @Mapping(target = "password", ignore = true) // Don't map password in DTO
    UserEntity toEntity(UserDTO userDTO);
}