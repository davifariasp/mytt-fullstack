package com.example.backend_postly.repositories;

import com.example.backend_postly.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
