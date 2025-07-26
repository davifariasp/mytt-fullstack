package com.example.mytt.application.repositories;

import com.example.mytt.adapters.repositories.UserRepository;
import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.application.mappers.UserMapper;
import com.example.mytt.core.domain.entities.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;
  private final UserMapper userMapper;

  @Override
  public User findUserByEmail() {
    return null;
  }

  @Override
  public User findUserByKeycloakUserId(UUID keycloakUserId) {
    return null;
  }

  @Override
  public User createUser(UserEntity userEntity) {
    var savedUser = userJpaRepository.save(userEntity);
    return userMapper.toDomain(savedUser);
  }
}
