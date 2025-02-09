DROP TABLE IF EXISTS `tenants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenants` (
    `id` VARCHAR(36) NOT NULL DEFAULT (UUID()),
    `name` VARCHAR(128) NOT NULL,
    `description` TEXT,
    `secret` VARCHAR(128) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `created_by` VARCHAR(36) NOT NULL,
    `disabled` BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `users` (
    `id` varchar(36) NOT NULL DEFAULT (UUID()),
    `user_name` varchar(64) NOT NULL,
    `first_name` varchar(64) NOT NULL,
    `last_name` varchar(64) NOT NULL,
    `email` varchar(64) NOT NULL,
    `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `enabled` tinyint(1) DEFAULT '0' NOT NULL,
    `locked` tinyint(1) DEFAULT '0' NOT NULL,
    `tenant_id` varchar(36) NOT NULL,
    `password` varchar(128) NOT NULL,
    FOREIGN KEY (`tenant_id`) REFERENCES `tenants`(`id`),
    UNIQUE KEY(user_name, tenant_id),
    UNIQUE KEY (email, tenant_id),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `authorities` (
    `id` varchar(36) NOT NULL DEFAULT (UUID()),
    `authority` varchar(64) NOT NULL,
    `tenant_id` varchar(36) NOT NULL,
    UNIQUE KEY (authority, tenant_id),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `users_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `users_authorities` (
    `user_id` varchar(36) NOT NULL,
    `authority_id` varchar(36) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`authority_id`) REFERENCES `authorities`(id)
);

alter table users_authorities add constraint `idx_user_authority` UNIQUE (user_id, authority_id);

CREATE TABLE `registered_clients` (
            `id` VARCHAR(36) NOT NULL DEFAULT (UUID()),
            `client_id` varchar(36) NOT NULL,
            `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
            `client_secret` varchar(512) NOT NULL,
            `client_authentication_method` varchar(64) NOT NULL,
            `authorization_grant_types` varchar(256) NOT NULL,
            `redirect_uris` varchar(256) NOT NULL,
            `post_logout_redirect_uris` varchar(256) NOT NULL,
            `scopes` varchar(64) NOT NULL,
            `require_authorization_consent` bool NOT NULL DEFAULT FALSE,
            `require_proof_key` bool NOT NULL DEFAULT FALSE,
            `id_token_signature_algorithm` varchar(32) NOT NULL,
            `reuse_refresh_token` bool NOT NULL DEFAULT TRUE,
            `access_token_time_to_live` INT NOT NULL,
            `refresh_token_time_to_live` INT NOT NULL,
            `authorization_code_time_to_live` INT NOT NULL,
            `oauth2_token_format` varchar(32) NOT NULL,
            UNIQUE (`client_id`),
            PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `authorizations`;
create table `authorizations` (
            `id` VARCHAR(36) NOT NULL,
            `registered_client_id` varchar(36) NOT NULL,
            `principal_name` varchar(256) NOT NULL,
            `authorization_grant_type` varchar(64) NOT NULL,
            `authorized_scopes` varchar(256) NOT NULL,
            `attributes` TEXT NOT NULL,
            `state` varchar(256),
            `authorization_code_value` varchar(256),
            `authorization_code_issued_at` timestamp,
            `authorization_code_expires_at` timestamp,
            `authorization_code_metadata` TEXT,
            `access_token_value` varchar(1024),
            `access_token_issued_at` timestamp,
            `access_token_expires_at` timestamp,
            `access_token_metadata` TEXT,
            `access_token_type` varchar(32),
            `access_token_scopes` varchar(256),
            `refresh_token_value` varchar(256),
            `refresh_token_issued_at` timestamp,
            `refresh_token_expires_at` timestamp,
            `refresh_token_metadata` TEXT,
            `oidc_id_token_value` varchar(1024),
            `oidc_id_token_issued_at` timestamp,
            `oidc_id_token_expires_at` timestamp,
            `oidc_id_token_metadata` TEXT,
            `oidc_id_token_claims` TEXT,
            `user_code_value` varchar(64),
            `device_code_value` varchar(64),
            `device_code_issued_at` timestamp,
            `device_code_expires_at` timestamp,
            `device_code_metadata` TEXT,
            INDEX `idx_authorizations_refresh_token_value` (`refresh_token_value`),
            INDEX `idx_authorizations_authorization_code_value` (`authorization_code_value`),
            FOREIGN KEY (registered_client_id) REFERENCES registered_clients(id),
            PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;