package com.example.mytt.adapters.usecases.post;

import com.example.mytt.core.domain.entities.Post;
import java.util.List;

public interface ListAllPostsUseCase {
  List<Post> execute();
}
