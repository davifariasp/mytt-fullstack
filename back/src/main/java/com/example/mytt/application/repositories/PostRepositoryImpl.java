package com.example.mytt.application.repositories;

import com.example.mytt.adapters.repositories.PostRepository;
import com.example.mytt.application.entities.PostEntity;
import com.example.mytt.core.domain.entities.Post;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl implements PostRepository {

  private final PostJpaRepository postJpaRepository;
  private final UserJpaRepository userJpaRepository;

  public PostRepositoryImpl(
      UserJpaRepository userJpaRepository, PostJpaRepository postJpaRepository) {
    this.userJpaRepository = userJpaRepository;
    this.postJpaRepository = postJpaRepository;
  }

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
  }
}
