package com.example.mytt.adapters.usecases.post;

import com.example.mytt.core.domain.entities.Post;
import java.util.List;

public interface GetPostUseCase {
  Post findPostById(Long postId);

  List<Post> findAllPostsByUserId(Long userId);

  List<Post> findAllPosts();
}
