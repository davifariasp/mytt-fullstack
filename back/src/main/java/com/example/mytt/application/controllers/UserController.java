package com.example.mytt.application.controllers;

import com.example.mytt.adapters.usecases.user.AuthenticateUserUseCase;
import com.example.mytt.adapters.usecases.user.CreateUserUseCase;
import com.example.mytt.adapters.usecases.user.GetUserUseCase;
import com.example.mytt.application.dtos.CreateUserRequest;
import com.example.mytt.application.dtos.LoginRequest;
import com.example.mytt.application.dtos.LoginResponse;
import com.example.mytt.core.domain.entities.User;
import com.example.mytt.core.enums.RolesEnum;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
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

  final CreateUserUseCase createUserUseCase;
  final GetUserUseCase getUserUseCase;
  final AuthenticateUserUseCase authenticateUserUseCase;

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
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(
        authenticateUserUseCase.execute(loginRequest.email(), loginRequest.password()));
  }

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(getUserUseCase.findAllUsers());
  }
}
