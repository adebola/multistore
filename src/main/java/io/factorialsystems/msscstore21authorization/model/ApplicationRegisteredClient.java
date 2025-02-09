package io.factorialsystems.msscstore21authorization.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ApplicationRegisteredClient {
    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private String clientAuthenticationMethod;
    private String authorizationGrantTypes;
    private String redirectUris;
    private String postLogoutRedirectUris;
    private String scopes;
    private Boolean requireAuthorizationConsent;
    private Boolean requireProofKey;
    private String idTokenSignatureAlgorithm;
    private Boolean reuseRefreshToken;
    private Long accessTokenTTL;
    private Long refreshTokenTTL;
    private Long authorizationCodeTTL;
    private String oAuth2TokenFormat;
}
