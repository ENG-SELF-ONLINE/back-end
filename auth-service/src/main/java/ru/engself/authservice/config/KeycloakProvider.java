package ru.engself.authservice.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientID)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientID)
                .clientSecret(clientSecret)
                .username(username)
                .password(password);
    }

    public JsonNode refreshToken(String refreshToken) throws UnirestException {
        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", clientID)
                .field("client_secret", clientSecret)
                .field("refresh_token", refreshToken)
                .field("grant_type", "refresh_token")
                .asString();
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new UnirestException("Error parsing JSON response: " + e.getMessage());
        }
    }

    public void logout(String refreshToken) {
        String url = serverURL + "/realms/" + realm + "/protocol/openid-connect/logout";

        try {
            Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("client_id", clientID)
                    .field("client_secret", clientSecret)
                    .field("refresh_token", refreshToken)
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException("Logout failed", e);
        }
    }
}