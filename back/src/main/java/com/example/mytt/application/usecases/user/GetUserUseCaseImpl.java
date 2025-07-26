package com.example.mytt.application.usecases.user;

import com.example.mytt.adapters.repositories.UserRepository;
import com.example.mytt.adapters.usecases.user.GetUserUseCase;
import com.example.mytt.core.domain.entities.User;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {

  private final UserRepository userRepository;

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public User findUserByKeycloakUserId(UUID keycloakUserId) {
    return userRepository.findUserByKeycloakUserId(keycloakUserId);
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAllUsers();
  }
}
