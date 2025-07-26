package com.example.mytt.adapters.usecases.user;

import com.example.mytt.application.dtos.LoginResponse;

public interface AuthenticateUserUseCase {
  LoginResponse execute(String email, String password);
}
