package com.example.mytt.adapters.usecases.post;

import com.example.mytt.core.domain.entities.Post;

public interface CreatePostUseCase {
  Post execute(String content, Long userId);
}
