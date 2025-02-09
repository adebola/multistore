package io.factorialsystems.msscstore21authorization.service;

import io.factorialsystems.msscstore21authorization.dto.RegisterUserDTO;
import io.factorialsystems.msscstore21authorization.exception.UserExistsException;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@CommonsLog
@SpringBootTest
class JpaUserDetailsServiceTestIT {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
        final UserDetails adebola = userDetailsService.loadUserByUsername("adebola");
        log.info(adebola);
    }

    @Test
    void createUser_NoRoles_Exception() {
        Exception exception = assertThrows(UserExistsException.class, () -> {
            RegisterUserDTO registerUserDTO = new RegisterUserDTO();
            registerUserDTO.setUserName("damola");
            registerUserDTO.setFirstName("Adedamola");
            registerUserDTO.setLastName("Omoboya");
            registerUserDTO.setEmail("damola@omoboya.com");
            registerUserDTO.setPassword("password");

            userDetailsService.createUser(registerUserDTO);
        });
    }

    @Test
    @Transactional
    @Rollback
    void createUser_Roles() {
        Set<String> roles = Set.of("User");

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUserName("oyinda");
        registerUserDTO.setFirstName("Oyindamola");
        registerUserDTO.setLastName("Omoboya");
        registerUserDTO.setEmail("oyindamola@omoboya.com");
        registerUserDTO.setPassword("password");
        registerUserDTO.setMatchingPassword("password");

        userDetailsService.createUser(registerUserDTO);
    }
}