package io.factorialsystems.msscstore21authorization.service;

import io.factorialsystems.msscstore21authorization.config.RedisConfig;
import io.factorialsystems.msscstore21authorization.model.Tenant;
import io.factorialsystems.msscstore21authorization.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaTenantService {
    private final TenantRepository tenantRepository;

    @Cacheable(value = RedisConfig.TENANT_CACHE_NAME, key = "#id", unless = "#result == null")
    public Tenant findById(String id) {
        log.info("Tenant::FindById : {}", id);
        return tenantRepository.findById(id).orElse(null);
    }
}
