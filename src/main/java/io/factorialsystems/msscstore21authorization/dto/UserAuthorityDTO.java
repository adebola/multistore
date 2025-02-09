package io.factorialsystems.msscstore21authorization.dto;

import io.factorialsystems.msscstore21authorization.model.UserAuthority;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityDTO {
    @Null(message = "User DTO id cannot be set it will be generated automatically")
    private Long id;
    private String authority;

    public UserAuthority createEntity() {
        return new UserAuthority(authority);
    }
}
