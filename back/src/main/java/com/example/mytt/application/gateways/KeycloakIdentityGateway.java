package com.example.mytt.application.gateways;

import com.example.mytt.adapters.services.IdentityProviderPort;
import com.example.mytt.adapters.usecases.user.GetUserUseCase;
import com.example.mytt.application.dtos.LoginResponse;
import com.example.mytt.core.enums.RolesEnum;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class KeycloakIdentityGateway implements IdentityProviderPort {

  private final RealmResource keycloakRealm;
  private final String realmName;
  private final RestClient restClient;

  public KeycloakIdentityGateway(
      RealmResource keycloakRealm,
      @Value("${keycloak.realm.url}") String realmUrl,
      @Value("${keycloak.realm.name}") String realmName,
      GetUserUseCase getUserUseCase) {
    this.keycloakRealm = keycloakRealm;
    this.realmName = realmName;
    this.restClient = RestClient.builder().baseUrl(realmUrl).build();
  }

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

  @Override
  public LoginResponse login(String username, String password) {
    try {
      var keycloakResponse = generateCredentials(username, password);
      var accessToken = (String) keycloakResponse.get("access_token");
      var refreshToken = (String) keycloakResponse.get("refresh_token");

      return new LoginResponse(accessToken, refreshToken);
    } catch (Exception apiException) {
      throw apiException;
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

  private void setPassword(String userId, String password) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(password);

    keycloakRealm.users().get(userId).resetPassword(credential);
  }

  private void setRole(String userId, RolesEnum role) {
    RoleRepresentation roleRepresentation =
        keycloakRealm.roles().get(role.name()).toRepresentation();
    keycloakRealm.users().get(userId).roles().realmLevel().add(List.of(roleRepresentation));
  }

  private Map generateCredentials(String username, String password) {

    MultiValueMap<String, String> keycloakLoginRequest = new LinkedMultiValueMap();
    keycloakLoginRequest.add("client_id", realmName);
    keycloakLoginRequest.add("grant_type", "password");
    keycloakLoginRequest.add("scope", "openid");
    keycloakLoginRequest.add("username", username);
    keycloakLoginRequest.add("password", password);

    try {
      return restClient
          .post()
          .uri("/protocol/openid-connect/token")
          .body(keycloakLoginRequest)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .retrieve()
          .toEntity(Map.class)
          .getBody();
    } catch (HttpClientErrorException.Unauthorized e) {
      throw new RuntimeException("Credenciais incorretas!", e);
    } catch (Exception e) {
      throw new RuntimeException(
          "Erro inesperado verificar credenciais. Tente novamente em instantes.", e);
    }
  }
}
