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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @GetMapping("/hello-world")
    public ResponseEntity helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser() {
        // Recupera o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Jwt jwt = (Jwt) authentication.getPrincipal(); // O JWT completo
            String username = jwt.getClaimAsString("sub"); // Substitua "sub" pelo claim correto do seu token

            return ResponseEntity.ok("Usuário logado: " + username);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        var keycloakUserId = keycloakService.createUserInKeycloak(createUserRequest, RolesEnum.USER);

        var user = new User();
        user.setUsername(createUserRequest.username());
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
        user.setUsername(createUserRequest.username());
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
