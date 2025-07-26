package com.example.mytt.application.repositories;

import com.example.mytt.adapters.repositories.UserRepository;
import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.application.mappers.UserMapper;
import com.example.mytt.core.domain.entities.User;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;
  private final UserMapper userMapper;

  @Override
  public User findUserByEmail(String email) {
    return userJpaRepository.findByEmail(email).map(userMapper::toDomain).orElse(null);
  }

  @Override
  public User findUserByKeycloakUserId(UUID keycloakUserId) {
    return userJpaRepository
        .findByKeycloakUserId(keycloakUserId)
        .map(userMapper::toDomain)
        .orElse(null);
  }

  @Override
  public User createUser(UserEntity userEntity) {
    var savedUser = userJpaRepository.save(userEntity);
    return userMapper.toDomain(savedUser);
  }

  @Override
  public List<User> findAllUsers() {
    return userJpaRepository.findAll().stream().map(userMapper::toDomain).toList();
  }
}
