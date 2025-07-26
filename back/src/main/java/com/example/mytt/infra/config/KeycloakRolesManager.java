package com.example.mytt.infra.config;

import com.example.mytt.application.services.KeycloakIdentityGateway;
import com.example.mytt.core.enums.RolesEnum;
import java.util.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class KeycloakRolesManager implements ApplicationListener<ApplicationReadyEvent> {

  private final KeycloakIdentityGateway keycloakService;

  public KeycloakRolesManager(KeycloakIdentityGateway keycloakService) {
    this.keycloakService = keycloakService;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent ignored) {
    System.out.println("Criando roles..");

    List<RolesEnum> roles = List.of(RolesEnum.ADMIN, RolesEnum.USER);

    for (RolesEnum role : roles) {
      keycloakService.createRoleInKeycloak(role.name(), role.getDescricao());
    }
  }
}
