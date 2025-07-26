package com.example.mytt.application.services;

import com.example.mytt.adapters.services.IdentityProviderPort;
import com.example.mytt.core.enums.RolesEnum;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakIdentityGateway implements IdentityProviderPort {

  private final RealmResource keycloakRealm;

  @Override
  public UUID createUser(String username, String email, String password, RolesEnum role) {
    UserRepresentation user = new UserRepresentation();
    user.setEmail(email);
    user.setUsername(username);
    user.setEnabled(true);
    user.setEmailVerified(true);

    // try catch pois é uma operação que pode falhar
    try (Response response = keycloakRealm.users().create(user); ) {
      String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

      setRole(userId, role);
      setPassword(userId, password);

      return UUID.fromString(userId);
    } catch (Exception e) {
      throw new RuntimeException("Error creating user in Keycloak");
    }
  }

  public void createRoleInKeycloak(String name, String description) {
    var roleRepresentation = new RoleRepresentation();

    roleRepresentation.setName(name);
    roleRepresentation.setDescription(description);

    try {
      keycloakRealm.roles().create(roleRepresentation);
    } catch (ClientErrorException e) {
      // if exception is a 409 conflict it means the role already exists - ignoring
      if (e.getResponse().getStatus() != 409) {
        throw e;
      }
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
