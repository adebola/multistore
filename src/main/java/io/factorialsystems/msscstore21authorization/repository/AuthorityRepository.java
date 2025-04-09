package io.factorialsystems.msscstore21authorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscstore21authorization.model.UserAuthority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AuthorityRepository {
    List<UserAuthority> findAuthorities(List<String> item);
    List<UserAuthority> findDefaultRoleForTenant(String id);
    Page<UserAuthority> findAll();
    Optional<UserAuthority> findById(String id);
    Optional<UserAuthority> findByAuthority(String authority);
    void save (UserAuthority userAuthority);
    void update(UserAuthority userAuthority);
}
