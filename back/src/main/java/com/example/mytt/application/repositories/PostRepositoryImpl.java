package com.example.mytt.application.repositories;

import com.example.mytt.adapters.repositories.PostRepository;
import com.example.mytt.application.entities.PostEntity;
import com.example.mytt.application.mappers.PostMapper;
import com.example.mytt.core.domain.entities.Post;
import java.util.List;
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

  @Override
  public Post findPostById(Long postId) {
    return postJpaRepository
        .findById(postId)
        .map(postMapper::toDomain)
        .orElseThrow(() -> new RuntimeException("Post not found"));
  }

  @Override
  public List<Post> findAllPostsByUserId(Long userId) {
    return postJpaRepository.findAll().stream()
        .filter(post -> post.getUserEntity().getId().equals(userId))
        .map(postMapper::toDomain)
        .toList();
  }

  @Override
  public List<Post> findAllPosts() {
    return postJpaRepository.findAll().stream().map(postMapper::toDomain).toList();
  }
}
