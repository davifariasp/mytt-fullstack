package com.example.mytt.application.mappers;

import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.core.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toDomain(UserEntity userEntity);

  UserEntity toEntity(User user);
}
