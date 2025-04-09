package io.factorialsystems.msscstore21authorization.dto;

public record AuditRequestDTO(String action, String message, String userName, String tenantId) { }
