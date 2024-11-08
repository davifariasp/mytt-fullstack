package com.example.mytt.dtos;

import java.time.Instant;

public record PostResponse (
    Long id,
    String content,
    Instant createdAt,
    String user
) {}
