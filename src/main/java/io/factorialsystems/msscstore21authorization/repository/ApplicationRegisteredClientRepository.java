package io.factorialsystems.msscstore21authorization.repository;

import io.factorialsystems.msscstore21authorization.model.ApplicationRegisteredClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationRegisteredClientRepository {
    List<ApplicationRegisteredClient> findAll();
    ApplicationRegisteredClient findByClientId(String id);
    ApplicationRegisteredClient findById(String id);
    void save(ApplicationRegisteredClient client);
    void update(ApplicationRegisteredClient client);
}