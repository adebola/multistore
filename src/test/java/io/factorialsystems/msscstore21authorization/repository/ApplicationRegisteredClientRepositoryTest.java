package io.factorialsystems.msscstore21authorization.repository;

import io.factorialsystems.msscstore21authorization.model.ApplicationRegisteredClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApplicationRegisteredClientRepositoryTest {

    @Autowired
    private ApplicationRegisteredClientRepository repository;

    @Test
    void findAll() {}

    @Test
    void findByClientId() {}

    @Test
    void findById() {}

    @Test
    @Rollback
    @Transactional
    void save() {
        final String clientId = "test-client";
        final String clientSecret = "test-client-secret";
        final String clientAuthenticationMethod = "client_secret_basic";
        final String scopes = "openid,profile,email";
        final String authorizationGrantTypes = "authorization_code,refresh_token";
        final String redirectUri = "http://localhost:8080/login/oauth2/code/gateway";
        final String postLogoutRedirectUri = "http://127.0.0.1:8080/logged-out";

        ApplicationRegisteredClient applicationRegisteredClient = new ApplicationRegisteredClient();
        applicationRegisteredClient.setClientSecret(clientSecret);
        applicationRegisteredClient.setClientId(clientId);
        applicationRegisteredClient.setClientAuthenticationMethod(clientAuthenticationMethod);
        applicationRegisteredClient.setScopes(scopes);
        applicationRegisteredClient.setAuthorizationGrantTypes(authorizationGrantTypes);
        applicationRegisteredClient.setRedirectUris(redirectUri);
        applicationRegisteredClient.setPostLogoutRedirectUris(postLogoutRedirectUri);

        // Client Settings
        applicationRegisteredClient.setRequireAuthorizationConsent(false);
        applicationRegisteredClient.setRequireProofKey(false);

        // Token Settings
        applicationRegisteredClient.setAccessTokenTTL(86400L);
        applicationRegisteredClient.setAuthorizationCodeTTL(3600L);
        applicationRegisteredClient.setRefreshTokenTTL(3600L);
        applicationRegisteredClient.setReuseRefreshToken(true);
        applicationRegisteredClient.setIdTokenSignatureAlgorithm("RS256");
        applicationRegisteredClient.setOAuth2TokenFormat("SELF-CONTAINED");

        repository.save(applicationRegisteredClient);

        ApplicationRegisteredClient client = repository.findByClientId(clientId);

        log.info("registered client Id {}", client.getId());
        assertThat(client.getClientId()).isEqualTo(clientId);
        assertThat(client.getClientIdIssuedAt()).isNotNull();
        assertThat(client.getClientSecret()).isEqualTo(clientSecret);
        assertThat(client.getClientAuthenticationMethod()).isEqualTo(clientAuthenticationMethod);
        assertThat(client.getScopes()).isEqualTo(scopes);
        assertThat(client.getAuthorizationGrantTypes()).isEqualTo(authorizationGrantTypes);
        assertThat(client.getRedirectUris()).isEqualTo(redirectUri);
        assertThat(client.getPostLogoutRedirectUris()).isEqualTo(postLogoutRedirectUri);

        log.info("Client {}", client);

        // FindById
        final ApplicationRegisteredClient byId = repository.findById(client.getId());
        assertThat(byId).isNotNull();
        assertThat(byId.getClientId()).isEqualTo(clientId);

        // FindByClientId
        final ApplicationRegisteredClient byClientId = repository.findByClientId(clientId);
        assertThat(byClientId).isNotNull();
        assertThat(byClientId.getId()).isEqualTo(client.getId());
    }

    @Test
    void update() {}
}