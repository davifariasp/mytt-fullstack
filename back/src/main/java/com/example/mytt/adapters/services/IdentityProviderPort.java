package com.example.mytt.adapters.services;

import com.example.mytt.application.dtos.LoginResponse;
import com.example.mytt.core.enums.RolesEnum;
import java.util.UUID;

public interface IdentityProviderPort {
  UUID createUser(String username, String email, String password, RolesEnum role);

  LoginResponse login(String email, String password);
}
