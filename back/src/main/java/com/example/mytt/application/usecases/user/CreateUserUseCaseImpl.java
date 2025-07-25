package com.example.mytt.application.usecases.user;

import com.example.mytt.adapters.usecases.user.CreateUserUseCase;
import com.example.mytt.core.enums.RolesEnum;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

  private final RealmResource keycloakRealm;

  public CreateUserUseCaseImpl(RealmResource keycloakRealm) {
    this.keycloakRealm = keycloakRealm;
  }

  @Override
  public UUID execute(String username, String email, String password, RolesEnum role) {
    UserRepresentation user = new UserRepresentation();
    user.setEmail(email);
    user.setUsername(username);
    user.setEnabled(true);
    user.setEmailVerified(true);

    try (Response response = keycloakRealm.users().create(user); ) {
      String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

      setRole(userId, role);
      setPassword(userId, password);

      return UUID.fromString(userId);
    } catch (Exception e) {
      throw new RuntimeException("Error creating user in Keycloak");
    }
  }

  public void setPassword(String userId, String password) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(password);

    keycloakRealm.users().get(userId).resetPassword(credential);
  }

  public void setRole(String userId, RolesEnum role) {
    RoleRepresentation roleRepresentation =
        keycloakRealm.roles().get(role.name()).toRepresentation();
    keycloakRealm.users().get(userId).roles().realmLevel().add(List.of(roleRepresentation));
  }
}
