package com.example.mytt.application.repositories;

import com.example.mytt.application.entities.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByKeycloakUserId(UUID keycloakUserId);
}
