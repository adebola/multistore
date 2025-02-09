package io.factorialsystems.msscstore21authorization.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TenantResponseDTO {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private String createdBy;
}
