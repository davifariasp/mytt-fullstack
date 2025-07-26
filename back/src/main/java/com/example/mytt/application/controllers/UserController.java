package com.example.mytt.application.controllers;

import com.example.mytt.adapters.usecases.user.CreateUserUseCase;
import com.example.mytt.application.dtos.CreateUserRequest;
import com.example.mytt.application.dtos.LoginRequest;
import com.example.mytt.application.repositories.UserJpaRepository;
import com.example.mytt.application.services.LoginService;
import com.example.mytt.core.domain.entities.User;
import com.example.mytt.core.enums.RolesEnum;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserController {

  final UserJpaRepository userJpaRepository;
  final LoginService loginService;

  final CreateUserUseCase createUserUseCase;

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
      String username =
          jwt.getClaimAsString("sub"); // Substitua "sub" pelo claim correto do seu token

      return ResponseEntity.ok("Usuário logado: " + username);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

    var user =
        createUserUseCase.execute(
            createUserRequest.username(),
            createUserRequest.email(),
            createUserRequest.password(),
            RolesEnum.USER);

    return ResponseEntity.ok(user);
  }

  @PostMapping("/admins")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<User> createAdmin(@Valid @RequestBody CreateUserRequest createUserRequest) {

    var user =
        createUserUseCase.execute(
            createUserRequest.username(),
            createUserRequest.email(),
            createUserRequest.password(),
            RolesEnum.ADMIN);

    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(loginService.login(loginRequest));
  }

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity getUsers() {
    return ResponseEntity.ok(userJpaRepository.findAll());
  }
}
