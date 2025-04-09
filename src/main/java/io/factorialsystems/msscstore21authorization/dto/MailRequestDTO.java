package io.factorialsystems.msscstore21authorization.dto;

public record MailRequestDTO(
        String to,
        String subject,
        String message,
        String tenantId
) { }
