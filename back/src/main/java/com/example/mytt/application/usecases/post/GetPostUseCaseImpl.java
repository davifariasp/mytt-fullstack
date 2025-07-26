package com.example.mytt.application.usecases.post;

import com.example.mytt.adapters.repositories.PostRepository;
import com.example.mytt.adapters.usecases.post.GetPostUseCase;
import com.example.mytt.core.domain.entities.Post;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetPostUseCaseImpl implements GetPostUseCase {

  private final PostRepository postRepository;

  @Override
  public Post findPostById(Long postId) {
    return postRepository.findPostById(postId);
  }

  @Override
  public List<Post> findAllPostsByUserId(Long userId) {
    return postRepository.findAllPostsByUserId(userId);
  }

  @Override
  public List<Post> findAllPosts() {
    return postRepository.findAllPosts();
  }
}
