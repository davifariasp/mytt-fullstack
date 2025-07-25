package com.example.mytt.application.services;

import com.example.mytt.application.dtos.PagePostReponse;
import com.example.mytt.application.dtos.PostResponse;
import com.example.mytt.application.entities.PostEntity;
import com.example.mytt.application.repositories.PostJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  @Autowired private PostJpaRepository postJpaRepository;

  public PagePostReponse findAllPostsByUserId(Long userId, int pagina) {

    PageRequest pageRequest = PageRequest.of(pagina, 10, Sort.by(Sort.Order.desc("createdAt")));

    Page<PostEntity> posts = postJpaRepository.findAllByUserId(userId, pageRequest);

    List<PostResponse> postResponses =
        posts.stream()
            .map(
                postEntity ->
                    new PostResponse(
                        postEntity.getId(),
                        postEntity.getContent(),
                        postEntity.getCreatedAt(),
                        postEntity.getUserEntity().getUsername()))
            .collect(Collectors.toList());

    return new PagePostReponse(posts.getNumber(), posts.getTotalPages(), postResponses);
  }
}
