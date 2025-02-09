package io.factorialsystems.msscstore21authorization.service;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class ResolveAuthorizationGrantType {
    public static AuthorizationGrantType resolve(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }

        // Custom authorization grant type
        return new AuthorizationGrantType(authorizationGrantType);
    }
}
