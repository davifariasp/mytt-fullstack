package com.example.backend_postly.services;

import com.example.backend_postly.dtos.CreateUserRequest;
import com.example.backend_postly.enums.RolesEnum;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final RealmResource keycloakRealm;

    public UUID createUserInKeycloak(CreateUserRequest req, RolesEnum role) {
        UserRepresentation user = new UserRepresentation();
        user.setEmail(req.email());
        user.setUsername(req.email());
        user.setEnabled(true);
        user.setAttributes(Map.of("name", List.of(req.name())));
        user.setEmailVerified(true);

        try (Response response = keycloakRealm.users().create(user);) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            setRole(userId, role);
            setPassword(userId, req.password());

            return UUID.fromString(userId);
        } catch (Exception e){
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

    public void setPassword(String userId, String password){
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        keycloakRealm.users().get(userId).resetPassword(credential);
    }

    public void setRole(String userId, RolesEnum role){
        RoleRepresentation roleRepresentation = keycloakRealm.roles().get(role.name()).toRepresentation();
        keycloakRealm.users().get(userId).roles().realmLevel().add(List.of(roleRepresentation));
    }
}
