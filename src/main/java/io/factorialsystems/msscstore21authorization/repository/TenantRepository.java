package io.factorialsystems.msscstore21authorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscstore21authorization.model.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface TenantRepository {
    Optional<Tenant> findById(String id);
    Page<Tenant> findAll();
    void save(Tenant tenant);
    void update(Tenant tenant);
}
