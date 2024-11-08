package com.example.backend_postly.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotBlank @NotNull String content
) {
}
