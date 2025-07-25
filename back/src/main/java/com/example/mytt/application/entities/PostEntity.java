package com.example.mytt.application.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class PostEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "post_id")
  private Long id;

  private String content;
  @CreationTimestamp private Instant createdAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;
}
