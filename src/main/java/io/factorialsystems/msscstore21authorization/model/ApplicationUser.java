package io.factorialsystems.msscstore21authorization.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApplicationUser {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
    private Boolean locked;
    private String password;
    private Instant createdOn;
    private Set<UserAuthority> authorities;

    public static ApplicationUser create(String userName, String firstName, String lastName, String email, String password,  Set<UserAuthority> authorities) {
        ApplicationUser au = new ApplicationUser();
        au.id = UUID.randomUUID().toString();
        au.userName = userName;
        au.firstName = firstName;
        au.lastName = lastName;
        au.email = email;
        au.enabled = false;
        au.locked = true;
        au.password = password;
        au.authorities = authorities;

        return au;
    }

    public UserDetails toUserDetails() {
        final Set<GrantedAuthority> grantedAuthorities = this.authorities
                .stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority()))
                .collect(Collectors.toSet());

        return new User (
                this.userName,
                this.password,
                this.enabled,
                true,
                true,
                !this.locked,
                grantedAuthorities
        );
    }
}
