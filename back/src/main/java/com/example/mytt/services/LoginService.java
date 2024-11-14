package com.example.mytt.services;

import com.example.mytt.dtos.LoginRequest;
import com.example.mytt.dtos.LoginResponse;
import com.example.mytt.models.User;
import com.example.mytt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    private final String realmName;
    private final RestClient restClient;

    private final UserRepository userRepository;

    public LoginService(
            @Value("${keycloak.realm.url}") String realmUrl,
            @Value("${keycloak.realm.name}") String realmName,
            UserRepository userRepository) {
        this.realmName = realmName;
        this.restClient = RestClient.builder().baseUrl(realmUrl).build();
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            var keycloakResponse = generateCredentials(request);
            var accessToken = (String) keycloakResponse.get("access_token");
            var refreshToken = (String) keycloakResponse.get("refresh_token");

            return new LoginResponse(accessToken, refreshToken);
        } catch (Exception apiException) {
            throw apiException;
        }
    }

//    public LoginResponse refreshToken(RefreshTokenRequest request) {
//        MultiValueMap<String, String> keycloakRefreshRequest = new LinkedMultiValueMap();
//        keycloakRefreshRequest.add("client_id", realmName);
//        keycloakRefreshRequest.add("grant_type", "refresh_token");
//        keycloakRefreshRequest.add("refresh_token", request.refreshToken());
//
//        try {
//            var keycloakResponse =
//                    restClient
//                            .post()
//                            .uri("/protocol/openid-connect/token")
//                            .body(keycloakRefreshRequest)
//                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                            .retrieve()
//                            .toEntity(Map.class)
//                            .getBody();
//
//            var accessToken = (String) keycloakResponse.get("access_token");
//            var refreshToken = (String) keycloakResponse.get("refresh_token");
//            var expiresIn = (Integer) keycloakResponse.get("expires_in");
//            var refreshTokenExpiresIn = (Integer) keycloakResponse.get("refresh_expires_in");
//
//            return new RefreshTokenResponse(accessToken, refreshToken, expiresIn, refreshTokenExpiresIn);
//        } catch (HttpClientErrorException.Unauthorized e) {
//            throw new APIException(401, "Credenciais incorretas!", null);
//        } catch (HttpClientErrorException.BadRequest e) {
//            throw new APIException(400, "Refresh Token Inválido!", null);
//        } catch (Exception e) {
//            throw new APIException(500, "Erro inesperado ao realizar refresh", e);
//        }
//    }

    private Map generateCredentials(LoginRequest request) {

        Optional<User> user = userRepository.findByEmail(request.email());

        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        MultiValueMap<String, String> keycloakLoginRequest = new LinkedMultiValueMap();
        keycloakLoginRequest.add("client_id", realmName);
        keycloakLoginRequest.add("grant_type", "password");
        keycloakLoginRequest.add("scope", "openid");
        keycloakLoginRequest.add("username", user.get().getUsername());
        keycloakLoginRequest.add("password", request.password());

        try {
            var keycloakResponse =
                    restClient
                            .post()
                            .uri("/protocol/openid-connect/token")
                            .body(keycloakLoginRequest)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .retrieve()
                            .toEntity(Map.class)
                            .getBody();

            return keycloakResponse;
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Credenciais incorretas!", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado verificar credenciais. Tente novamente em instantes.", e);
        }
    }
}
