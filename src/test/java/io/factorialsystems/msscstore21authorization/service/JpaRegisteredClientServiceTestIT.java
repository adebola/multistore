package io.factorialsystems.msscstore21authorization.service;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@CommonsLog
@SpringBootTest
class JpaRegisteredClientServiceTestIT {

    @Autowired
    private JpaRegisteredClientService clientService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Rollback
    @Transactional
    void save() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("client2")
				.clientSecret(passwordEncoder.encode("secret"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:8080/login/oauth2/code/gateway")
				.postLogoutRedirectUri("http://localhost:8080/logged-out")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(false)
						.requireProofKey(false)
						.build())
				.tokenSettings(TokenSettings.builder()
						.reuseRefreshTokens(true)
						.idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
						.accessTokenTimeToLive(Duration.ofMillis(86400))
						.refreshTokenTimeToLive(Duration.ofMillis(3600))
						.authorizationCodeTimeToLive(Duration.ofMillis(3600))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.build())
				.build();

        clientService.save(oidcClient);

		final RegisteredClient registeredClient = clientService.findByClientId("client2");
		assertThat(registeredClient).isNotNull();
		assertThat(registeredClient.getId()).isNotNull();

		log.info(registeredClient);
	}

    @Test
    void findById() {
    }

    @Test
    void findByClientId() {
    }

    @Test
    void saveRegisteredClient() {
    }
}

//String tokenSettingString =
//		"{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}";