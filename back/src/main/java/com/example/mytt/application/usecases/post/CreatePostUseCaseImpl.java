package com.example.mytt.application.usecases.post;

import com.example.mytt.adapters.repositories.PostRepository;
import com.example.mytt.adapters.usecases.post.CreatePostUseCase;
import com.example.mytt.core.domain.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class CreatePostUseCaseImpl implements CreatePostUseCase {

  private final PostRepository postRepository;

  public CreatePostUseCaseImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Post execute(String content, Long userID) {
    return postRepository.createPost(content, userID);
  }
}
