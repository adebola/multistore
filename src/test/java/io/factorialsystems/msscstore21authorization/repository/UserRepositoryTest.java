package io.factorialsystems.msscstore21authorization.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUserName() {
        final String tenantId = "bd965121-7a80-4928-ab79-fd585063d6ab";
        final String userName = "dele";

        final Map<String, String> m = new HashMap<>();
        m.put("username", userName);
        m.put("tenantId", tenantId);
        var user = userRepository.findByUserNameAndTenantId(m);

        assertThat(user).isPresent();
        assertThat(user.get().getUserName()).isEqualTo(userName);

        log.info("User: {}", user.get());
    }

    @Test
    public void findByUserName_NotFound() {
        final String userName = "Not Available";
        final String tenantId = "bd965121-7a80-4928-ab79-fd585063d6ab";

        final Map<String, String> m = new HashMap<>();
        m.put("username", userName);
        m.put("tenantId", tenantId);

        var user = userRepository.findByUserNameAndTenantId(m);

        assertThat(user).isEmpty();
    }
}