package io.factorialsystems.msscstore21authorization.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUserName() {
        final String userName = "Developer";
        var user = userRepository.findByUserName(userName);

        assertThat(user).isPresent();
        assertThat(user.get().getUserName()).isEqualTo(userName);

        log.info("User: {}", user.get());
    }

    @Test
    public void findByUserName_NotFound() {
        final String userName = "Not Available";
        var user = userRepository.findByUserName(userName);

        assertThat(user).isEmpty();
    }
}