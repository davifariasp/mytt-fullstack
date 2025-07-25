package com.example.mytt.application.services;

import com.example.mytt.application.entities.UserEntity;
import com.example.mytt.application.repositories.UserJpaRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserJpaRepository userJpaRepository;

  public UserEntity findUserByJwt(JwtAuthenticationToken token) {
    String keycloakUserId = token.getToken().getClaimAsString("sub");
    return userJpaRepository.findByKeycloakUserId(UUID.fromString(keycloakUserId)).orElseThrow();
  }
}
