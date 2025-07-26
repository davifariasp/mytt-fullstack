package com.example.mytt.application.usecases.user;

import com.example.mytt.adapters.repositories.UserRepository;
import com.example.mytt.adapters.services.IdentityProviderPort;
import com.example.mytt.adapters.usecases.user.AuthenticateUserUseCase;
import com.example.mytt.application.dtos.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

  private final IdentityProviderPort identityProviderPort;
  private final UserRepository userRepository;

  @Override
  public LoginResponse execute(String email, String password) {

    var user = userRepository.findUserByEmail(email);

    return identityProviderPort.login(user.username(), password);
  }
}
