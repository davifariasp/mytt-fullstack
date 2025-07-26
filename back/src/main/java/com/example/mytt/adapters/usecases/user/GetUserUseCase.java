package com.example.mytt.adapters.usecases.user;

import com.example.mytt.core.domain.entities.User;
import java.util.List;
import java.util.UUID;

public interface GetUserUseCase {
  User findUserByEmail(String email);

  User findUserByKeycloakUserId(UUID keycloakUserId);

  List<User> findAllUsers();
}
