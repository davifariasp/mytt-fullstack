package com.example.mytt.services;

import com.example.mytt.models.User;
import com.example.mytt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByJwt (JwtAuthenticationToken token){
        String keycloakUserId = token.getToken().getClaimAsString("sub");
        return userRepository.findByKeycloakUserId(UUID.fromString(keycloakUserId)).orElseThrow();
    }
}
