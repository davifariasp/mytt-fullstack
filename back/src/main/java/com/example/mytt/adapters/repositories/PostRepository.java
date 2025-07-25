package com.example.mytt.adapters.repositories;

import com.example.mytt.core.domain.entities.Post;

public interface PostRepository {
  Post createPost(String content, Long userId);
}
