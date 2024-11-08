package com.example.mytt.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    private final String keycloakBaseUrl;
    private final String masterRealmName;
    private final String realmName;
    private final String adminClientId;
    private final String adminUsername;
    private final String adminPassword;

    public KeycloakAdminClientConfig(
            @Value("${keycloak.url}") String keycloakBaseUrl,
            @Value("${keycloak.admin.username}") String adminUsername,
            @Value("${keycloak.admin.password}") String adminPassword,
            @Value("${keycloak.admin.client-id}") String adminClientId,
            @Value("${keycloak.master.realm.name}") String masterRealmName,
            @Value("${keycloak.realm.name}") String realmName) {
        this.keycloakBaseUrl = keycloakBaseUrl;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminClientId = adminClientId;
        this.masterRealmName = masterRealmName;
        this.realmName = realmName;
    }

    @Bean
    RealmResource keycloak() {
        Keycloak keycloak =
                KeycloakBuilder.builder()
                        .serverUrl(keycloakBaseUrl)
                        .realm(masterRealmName)
                        .clientId(adminClientId)
                        .grantType(OAuth2Constants.PASSWORD)
                        .username(adminUsername)
                        .password(adminPassword)
                        .build();
        return keycloak.realm(realmName);
    }
}
