package com.example.mytt.adapters.usecases.user;

import com.example.mytt.core.domain.entities.User;
import com.example.mytt.core.enums.RolesEnum;

public interface CreateUserUseCase {
  User execute(String username, String email, String password, RolesEnum role);
}
