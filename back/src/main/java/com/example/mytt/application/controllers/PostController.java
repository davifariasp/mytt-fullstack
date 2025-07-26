package com.example.mytt.application.controllers;

import com.example.mytt.adapters.usecases.post.CreatePostUseCase;
import com.example.mytt.adapters.usecases.post.GetPostUseCase;
import com.example.mytt.adapters.usecases.user.GetUserUseCase;
import com.example.mytt.application.dtos.CreatePostRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "Authorization")
public class PostController {

  final CreatePostUseCase createPostUseCase;
  final GetUserUseCase getUserUseCase;
  final GetPostUseCase getPostUseCase;

  public PostController(
      CreatePostUseCase createPostUseCase,
      GetUserUseCase getUserUseCase,
      GetPostUseCase getPostUseCase) {
    this.createPostUseCase = createPostUseCase;
    this.getUserUseCase = getUserUseCase;
    this.getPostUseCase = getPostUseCase;
  }

  @PostMapping("/posts")
  public ResponseEntity createPost(
      @Valid @RequestBody CreatePostRequest createPostRequest, JwtAuthenticationToken token) {

    var user = getUserUseCase.findUserByKeycloakUserId(UUID.fromString(token.getName()));

    createPostUseCase.execute(createPostRequest.content(), user.id());

    return ResponseEntity.ok("Post criado com sucesso!");
  }

  @GetMapping("/posts")
  public ResponseEntity listAllPosts() {
    return ResponseEntity.ok(getPostUseCase.findAllPosts());
  }

  @GetMapping("/posts/user")
  public ResponseEntity listAllPostsByUser(
      @RequestParam(defaultValue = "0") int pagina, JwtAuthenticationToken token) {

    String keycloakUserId = token.getToken().getClaimAsString("sub");

    var user = getUserUseCase.findUserByKeycloakUserId(UUID.fromString(keycloakUserId));

    return ResponseEntity.ok(getPostUseCase.findAllPostsByUserId(user.id()));
  }
}
