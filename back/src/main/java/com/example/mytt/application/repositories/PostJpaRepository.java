package com.example.mytt.application.repositories;

import com.example.mytt.application.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
  Page<PostEntity> findAllByUserId(Long userId, Pageable pageable);
}
