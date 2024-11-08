package com.example.mytt.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotBlank @NotNull String content
) {
}
