package io.factorialsystems.msscstore21authorization.repository;

import io.factorialsystems.msscstore21authorization.model.Authorization;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AuthorizationRepository {
    void save(Authorization authorization);
    void update(Authorization authorization);
    void deleteById(String id);
    Optional<Authorization> findById(String id);
    Optional <Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(String token);
    Optional<Authorization> findByState(String state);
    Optional<Authorization> findByAuthorizationCodeValue(String authorizationCodeValue);
    Optional<Authorization> findByAccessTokenValue(String accessTokenValue);
    Optional<Authorization> findByRefreshTokenValue(String refreshTokenValue);
    Boolean IsExist(String id);

}
