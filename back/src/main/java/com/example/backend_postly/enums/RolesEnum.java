package com.example.backend_postly.enums;

import lombok.Getter;

public enum RolesEnum{
    ADMIN("ADMIN"),
    USER("USER");

    @Getter
    private final String descricao;

    RolesEnum(String descricao) {
        this.descricao = descricao;
    }
}