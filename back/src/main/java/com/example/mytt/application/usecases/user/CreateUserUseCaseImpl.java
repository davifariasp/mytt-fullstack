package com.example.mytt.application.usecases.user;

import com.example.mytt.adapters.repositories.UserRepository;
import com.example.mytt.adapters.services.IdentityProviderPort;
import com.example.mytt.adapters.usecases.user.CreateUserUseCase;
import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.core.domain.entities.User;
import com.example.mytt.core.enums.RolesEnum;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

  private final IdentityProviderPort identityProviderPort;
  private final UserRepository userRepository;

  @Override
  public User execute(String username, String email, String password, RolesEnum role) {

    UUID keycloackUserId = identityProviderPort.createUser(username, email, password, role);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setEmail(email);
    userEntity.setKeycloakUserId(keycloackUserId);
    userEntity.setPermissions(role.name());

    return userRepository.createUser(userEntity);
  }
}
