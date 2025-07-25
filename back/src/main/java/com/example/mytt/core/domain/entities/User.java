package com.example.mytt.core.domain.entities;

import java.util.UUID;

public record User(
    Long id, UUID keycloakUserId, String username, String email, String permissions) {}
