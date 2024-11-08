package com.example.backend_postly.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank @NotNull String name,
        @NotBlank @NotNull @Email String email,
        @NotBlank @NotNull String password
) {

}
