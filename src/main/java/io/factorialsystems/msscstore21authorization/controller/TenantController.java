package io.factorialsystems.msscstore21authorization.controller;

import io.factorialsystems.msscstore21authorization.dto.PagedDTO;
import io.factorialsystems.msscstore21authorization.dto.TenantRequestDTO;
import io.factorialsystems.msscstore21authorization.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21authorization.service.JpaTenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("/api/v1/tenant")
@RequiredArgsConstructor
public class TenantController {
    private final JpaTenantService tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody TenantRequestDTO tenant) {
        log.info("Saving tenant: {}", tenant);
        tenantService.save(tenant);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody TenantRequestDTO tenant) {
        log.info("Updating tenant: {}", tenant);
        tenantService.update(id, tenant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> findById(@PathVariable String id) {
        log.info("Find Tenant : {}", id);
        return ResponseEntity.ok(tenantService.findTenantById(id));
    }

    @GetMapping
    public ResponseEntity<PagedDTO<TenantResponseDTO>> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        log.info("Find All Tenants PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(tenantService.findAll(pageNumber, pageSize));
    }

    @PutMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable String id) {
        log.info("Disabling Tenant : {}", id);
        tenantService.disable(id);
    }

    @PutMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable String id) {
        log.info("Enabling Tenant : {}", id);
        tenantService.enable(id);
    }
}
