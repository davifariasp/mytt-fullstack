package com.example.mytt.controllers;

import com.example.mytt.dtos.CreatePostRequest;
import com.example.mytt.dtos.PostResponse;
import com.example.mytt.models.Post;
import com.example.mytt.repositories.PostRepository;
import com.example.mytt.repositories.UserRepository;
import com.example.mytt.services.PostService;
import com.example.mytt.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Authorization")
public class PostController {

    final PostRepository postRepository;

    final UserRepository userRepository;

    final PostService postService;

    final UserService userService;

    public PostController(PostRepository postRepository, UserRepository userRepository, PostService postService, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/posts")
    public ResponseEntity createPost(@Valid @RequestBody CreatePostRequest createPostRequest, JwtAuthenticationToken token) {
        var user = userRepository.findByKeycloakUserId(UUID.fromString(token.getName()));

        var post = new Post();
        post.setContent(createPostRequest.content());
        post.setUser(user.get());

        postRepository.save(post);

        return ResponseEntity.ok("Post criado com sucesso!");
    }

    @GetMapping("/posts")
    public ResponseEntity listAllPosts() {
        var posts = postRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));

        List<PostResponse> response = posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getUser().getUsername()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/user")
    public ResponseEntity listAllPostsByUser(@RequestParam(defaultValue = "0") int pagina, JwtAuthenticationToken token) {

        var user = userService.findUserByJwt(token);
        return ResponseEntity.ok(postService.findAllPostsByUserId(user.getId(), pagina));
    }
}
