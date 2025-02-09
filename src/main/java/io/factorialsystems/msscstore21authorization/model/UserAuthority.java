package io.factorialsystems.msscstore21authorization.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority {
    private String id;
    private String authority;

    public UserAuthority(String authority) {
        this.authority = authority;
    }
}
