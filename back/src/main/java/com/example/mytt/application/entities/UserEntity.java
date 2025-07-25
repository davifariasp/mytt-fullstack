package com.example.mytt.application.entities;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  private UUID keycloakUserId;

  @Column(unique = true)
  private String username;

  private String email;
  private String permissions;
}
