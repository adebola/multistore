package io.factorialsystems.msscstore21authorization.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscstore21authorization.config.RedisConfig;
import io.factorialsystems.msscstore21authorization.dto.PagedDTO;
import io.factorialsystems.msscstore21authorization.dto.TenantRequestDTO;
import io.factorialsystems.msscstore21authorization.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21authorization.mapper.TenantMapper;
import io.factorialsystems.msscstore21authorization.model.Tenant;
import io.factorialsystems.msscstore21authorization.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaTenantService {
    private final CacheManager cacheManager;
    private final TenantMapper tenantMapper;
    private final TenantRepository tenantRepository;

    public void save(TenantRequestDTO tenant) {
        log.info("Saving tenant: {}", tenant);
        tenantRepository.save(tenantMapper.toEntity(tenant));
    }

    public void update(String id, TenantRequestDTO tenant) {
        log.info("Updating tenant: {}", tenant);
        Tenant t = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        Tenant newTenant = tenantMapper.toEntity(tenant);
        newTenant.setId(t.getId());
        tenantRepository.update(newTenant);
        evictCache(id);
    }

    public PagedDTO<TenantResponseDTO> findAll(int pageNumber, int pageSize) {
        log.info("Find All Tenants PageNumber {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createDto(tenantRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Cacheable(value = RedisConfig.TENANT_CACHE_NAME, key = "#id", unless = "#result == null")
    public Tenant findById(String id) {
        log.info("Tenant::FindById : {}", id);
        return tenantRepository.findById(id).orElse(null);
    }

    public TenantResponseDTO findTenantById(String id) {
        log.info("Tenant::FindTenantById : {}", id);
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        return tenantMapper.toDto(tenant);
    }

    public void disable(String id) {
        log.info("Disabling Tenant : {}", id);
        Tenant t = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        t.setDisabled(true);
        tenantRepository.update(t);
        evictCache(id);
    }

    public void enable(String id) {
        log.info("Enabling Tenant : {}", id);
        Tenant t = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        t.setDisabled(false);
        tenantRepository.update(t);
        evictCache(id);
    }

    private void evictCache(String id) {
        log.info("Evicting Tenant Cache : {}", id);
        final Cache tenantCache = cacheManager.getCache(RedisConfig.TENANT_CACHE_NAME);
        if (tenantCache != null) tenantCache.evict(id);
    }

    private PagedDTO<TenantResponseDTO> createDto(Page<Tenant> tenants) {
        List<Tenant> result = tenants.getResult();

        PagedDTO<TenantResponseDTO> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) tenants.getTotal());
        pagedDto.setPageNumber(tenants.getPageNum());
        pagedDto.setPageSize(tenants.getPageSize());
        pagedDto.setPages(tenants.getPages());
        pagedDto.setList(result.stream().map(tenantMapper::toDto).toList());

        return pagedDto;
    }
}
