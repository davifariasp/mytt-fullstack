package com.example.mytt.controllers;

import com.example.mytt.dtos.CreateUserRequest;
import com.example.mytt.dtos.LoginRequest;
import com.example.mytt.enums.RolesEnum;
import com.example.mytt.models.User;
import com.example.mytt.repositories.UserRepository;
import com.example.mytt.services.KeycloakService;
import com.example.mytt.services.LoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Authorization")
public class UserController {

    final UserRepository userRepository;
    final LoginService loginService;
    final KeycloakService keycloakService;

    public UserController(UserRepository userRepository, LoginService loginService, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        var keycloakUserId = keycloakService.createUserInKeycloak(createUserRequest, RolesEnum.USER);

        var user = new User();
        user.setName(createUserRequest.name());
        user.setKeycloakUserId(keycloakUserId);
        user.setPermissions(RolesEnum.USER.name());
        user.setEmail(createUserRequest.email());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admins")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity createAdmin(@Valid @RequestBody CreateUserRequest createUserRequest) {

        var keycloakUserId = keycloakService.createUserInKeycloak(createUserRequest, RolesEnum.ADMIN);

        var user = new User();
        user.setName(createUserRequest.name());
        user.setKeycloakUserId(keycloakUserId);
        user.setPermissions(RolesEnum.ADMIN.name());
        user.setEmail(createUserRequest.email());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
