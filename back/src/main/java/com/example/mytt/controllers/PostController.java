package com.example.mytt.controllers;

import com.example.mytt.dtos.CreatePostRequest;
import com.example.mytt.dtos.PostResponse;
import com.example.mytt.models.Post;
import com.example.mytt.repositories.PostRepository;
import com.example.mytt.repositories.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Authorization")
public class PostController {

    final PostRepository postRepository;

    final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
}
