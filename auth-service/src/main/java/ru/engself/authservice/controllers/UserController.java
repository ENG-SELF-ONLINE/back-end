package ru.engself.authservice.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.authservice.config.KeycloakProvider;
import ru.engself.authservice.http.requests.CreateUserRequest;
import ru.engself.authservice.http.requests.LoginRequest;
import ru.engself.authservice.service.KeycloakAdminClientService;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakAdminClientService kcAdminClient;
    private final KeycloakProvider kcProvider;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestPart("userDTO") CreateUserRequest userDTO, @RequestPart("image") MultipartFile image) {
        try (Response createdResponse = kcAdminClient.createKeycloakUser(userDTO, image)) {
            return ResponseEntity.status(createdResponse.getStatus()).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        try (Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(), loginRequest.getPassword()).build()) {
            try {
                AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
                return ResponseEntity.ok(accessTokenResponse);
            } catch (BadRequestException ex) {
                LOG.warn("Invalid account. User probably hasn't verified email.", ex);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JsonNode> refreshToken(@RequestBody Map<String, String> requestBody) {
        try {
            String refreshToken = requestBody.get("refreshToken");
            LOG.info("Received refresh token: {}", refreshToken);
            JsonNode tokenResponse = kcProvider.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            LOG.error("Error during refresh token request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {
        try {
            kcProvider.logout(refreshToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOG.error("Logout failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}