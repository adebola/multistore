package io.factorialsystems.msscstore21authorization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class ApplicationUserDTO {
    @Null
    private String id;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private Boolean locked;
    private Boolean enabled;

    private Set<String> roles;
}
