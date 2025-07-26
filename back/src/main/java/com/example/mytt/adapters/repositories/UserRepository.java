package com.example.mytt.adapters.repositories;

import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.core.domain.entities.User;
import java.util.UUID;

public interface UserRepository {
  User findUserByEmail();

  User findUserByKeycloakUserId(UUID keycloakUserId);

  User createUser(UserEntity userEntity);
}
