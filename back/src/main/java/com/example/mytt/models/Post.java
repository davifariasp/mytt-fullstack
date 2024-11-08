package com.example.mytt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "post_id")
    private Long id;
    private String content;
    @CreationTimestamp
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
