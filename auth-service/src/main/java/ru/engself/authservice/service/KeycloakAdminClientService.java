package ru.engself.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.authservice.config.KeycloakProvider;
import ru.engself.authservice.dtos.UserDTO;
import ru.engself.authservice.enums.Level;
import ru.engself.authservice.http.requests.CreateUserRequest;
import ru.engself.authservice.utils.feigns.UserFeignController;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClientService {

    @Value("${keycloak.realm}")
    public String realm;
    private final KeycloakProvider kcProvider;
    private final UserFeignController userFeignController;
    private final ObjectMapper objectMapper;

    public Response createKeycloakUser(CreateUserRequest user, MultipartFile file) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            String userId = getUserId(usersResource, user.getEmail());
            if (userId != null) {
                UserDTO userDTO = UserDTO.builder()
                        .userId(UUID.fromString(userId))
                        .email(user.getEmail())
                        .firstName(user.getFirstname())
                        .lastName(user.getLastname())
                        .level(Level.A1)
                        .initialized(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                try {
                    String userDTOString = objectMapper.writeValueAsString(userDTO);
                    MultipartFile userDTOFile = new MockMultipartFile("userDTO", "userDTO",
                            MediaType.APPLICATION_JSON_VALUE, userDTOString.getBytes());
                    userFeignController.createUser(userDTOFile, file);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return response;
    }

    private String getUserId(UsersResource usersResource, String email) {
        List<UserRepresentation> users = usersResource.search(email);
        if (users != null && !users.isEmpty()) {
            return users.get(0).getId();
        }
        return null;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}