package io.factorialsystems.msscstore21authorization.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TenantRequestDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String secret;
}
