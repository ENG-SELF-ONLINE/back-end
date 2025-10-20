package ru.engself.trackerservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class AuthenticationUtils {

    public static UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            if (userId == null) {
                throw new RuntimeException("JWT does not contain a subject (sub) claim.");
            }
            try {
                return UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid UUID format in JWT subject claim.", e);
            }
        } else {
            throw new RuntimeException("Authentication principal is not a JWT.");
        }
    }

    public static String getAuthorizationHeader(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return "Bearer " + jwt.getTokenValue();
        } else {
            return null;
        }
    }

    public static String generateKeyPrefix(String prefix, UUID userId) {
        return String.format("%s:%s:", prefix, userId.toString());
    }

}
