package com.example.mytt.core.enums;

import lombok.Getter;

public enum RolesEnum {
  ADMIN("ADMIN"),
  USER("USER");

  @Getter private final String descricao;

  RolesEnum(String descricao) {
    this.descricao = descricao;
  }
}
