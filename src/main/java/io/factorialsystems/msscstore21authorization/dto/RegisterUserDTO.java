package io.factorialsystems.msscstore21authorization.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    @NotEmpty
    private String userName;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String matchingPassword;

    @NotEmpty
    private String tenantId;
}
