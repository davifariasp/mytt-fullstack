package com.example.mytt.application.repositories;

import com.example.mytt.adapters.repositories.PostRepository;
import com.example.mytt.application.entities.PostEntity;
import com.example.mytt.application.mappers.PostMapper;
import com.example.mytt.core.domain.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

  private final PostJpaRepository postJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private final PostMapper postMapper;

  @Override
  public Post createPost(String content, Long userId) {

    var user =
        userJpaRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    var post = new PostEntity();

    post.setContent(content);
    post.setUserEntity(user);

    var postSaved = postJpaRepository.save(post);

    return postMapper.toDomain(postSaved);
  }
}
