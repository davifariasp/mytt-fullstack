package com.example.mytt.services;

import com.example.mytt.dtos.PagePostReponse;
import com.example.mytt.dtos.PostResponse;
import com.example.mytt.models.Post;
import com.example.mytt.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public PagePostReponse findAllPostsByUserId(Long userId, int pagina) {

        PageRequest pageRequest = PageRequest.of(pagina, 10, Sort.by(Sort.Order.desc("createdAt")));

        Page<Post> posts = postRepository.findAllByUserId(userId, pageRequest);

        List<PostResponse> postResponses = posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        return new PagePostReponse(
                posts.getNumber(),
                posts.getTotalPages(),
                postResponses
        );
    }
}
