package com.example.mytt.adapters.usecases.user;

import com.example.mytt.core.enums.RolesEnum;
import java.util.UUID;

public interface CreateUserUseCase {
  UUID execute(String username, String email, String password, RolesEnum role);
}
