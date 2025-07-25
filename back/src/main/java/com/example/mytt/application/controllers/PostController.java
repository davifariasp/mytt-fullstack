package com.example.mytt.application.controllers;

import com.example.mytt.adapters.usecases.post.CreatePostUseCase;
import com.example.mytt.application.dtos.CreatePostRequest;
import com.example.mytt.application.dtos.PostResponse;
import com.example.mytt.application.repositories.PostJpaRepository;
import com.example.mytt.application.repositories.UserJpaRepository;
import com.example.mytt.application.services.PostService;
import com.example.mytt.application.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "Authorization")
public class PostController {

  final PostJpaRepository postJpaRepository;

  final UserJpaRepository userJpaRepository;

  final PostService postService;

  final UserService userService;

  final CreatePostUseCase createPostUseCase;

  public PostController(
      PostJpaRepository postJpaRepository,
      UserJpaRepository userJpaRepository,
      PostService postService,
      UserService userService,
      CreatePostUseCase createPostUseCase) {
    this.postJpaRepository = postJpaRepository;
    this.userJpaRepository = userJpaRepository;
    this.postService = postService;
    this.userService = userService;
    this.createPostUseCase = createPostUseCase;
  }

  @PostMapping("/posts")
  public ResponseEntity createPost(
      @Valid @RequestBody CreatePostRequest createPostRequest, JwtAuthenticationToken token) {
    var user =
        userJpaRepository
            .findByKeycloakUserId(UUID.fromString(token.getName()))
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    createPostUseCase.execute(createPostRequest.content(), user.getId());

    return ResponseEntity.ok("Post criado com sucesso!");
  }

  @GetMapping("/posts")
  public ResponseEntity listAllPosts() {
    var posts = postJpaRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));

    List<PostResponse> response =
        posts.stream()
            .map(
                postEntity ->
                    new PostResponse(
                        postEntity.getId(),
                        postEntity.getContent(),
                        postEntity.getCreatedAt(),
                        postEntity.getUserEntity().getUsername()))
            .toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/posts/user")
  public ResponseEntity listAllPostsByUser(
      @RequestParam(defaultValue = "0") int pagina, JwtAuthenticationToken token) {

    var user = userService.findUserByJwt(token);
    return ResponseEntity.ok(postService.findAllPostsByUserId(user.getId(), pagina));
  }
}
