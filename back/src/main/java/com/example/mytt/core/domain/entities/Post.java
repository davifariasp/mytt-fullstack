package com.example.mytt.core.domain.entities;

import java.time.Instant;

public record Post(Long id, String content, Instant createdAt, User user) {}
