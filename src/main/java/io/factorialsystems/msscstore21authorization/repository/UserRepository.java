package io.factorialsystems.msscstore21authorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscstore21authorization.model.ApplicationUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {
    void save(ApplicationUser applicationUser);

    void update(ApplicationUser applicationUser);

    Optional<ApplicationUser> findById(String id);

    Optional<ApplicationUser> findByUserName(String userName);

    Page<ApplicationUser> findAll();

    Page<ApplicationUser> search(String search);

    Boolean IsExistsByUserName(String userName);

    Boolean IsExistsByEmail(String email);
}
