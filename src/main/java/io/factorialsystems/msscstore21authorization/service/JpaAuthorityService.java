package io.factorialsystems.msscstore21authorization.service;


import io.factorialsystems.msscstore21authorization.config.RedisConfig;
import io.factorialsystems.msscstore21authorization.dto.UserAuthorityDTO;
import io.factorialsystems.msscstore21authorization.model.UserAuthority;
import io.factorialsystems.msscstore21authorization.exception.AuthorityDoesNotExistException;
import io.factorialsystems.msscstore21authorization.exception.AuthorityExistsException;
import io.factorialsystems.msscstore21authorization.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class JpaAuthorityService {
    private final CacheManager cacheManager;
    private final AuthorityRepository authorityRepository;

    @Cacheable(value = RedisConfig.AUTHORITY_CACHE_NAME, unless = "#result == null")
    public List<UserAuthority> findAll() {
        log.info("Retrieving List of Roles from database and caching");
        return authorityRepository.findAll();
    }

    public void createAuthority(UserAuthorityDTO userAuthority) {
        authorityRepository.findByAuthority(userAuthority.getAuthority())
                .ifPresentOrElse((value) -> {
                    final String s = String.format("Role %s already exists", value.getAuthority());
                    log.error(s);
                    throw new AuthorityExistsException(s);
                }, () -> {
                    log.info("Creating UserAuthority {}", userAuthority);
                    authorityRepository.save(userAuthority.createEntity());
                    invalidateCache();
                });
    }

    public void editAuthority(String id, UserAuthorityDTO userAuthority) {
        authorityRepository.findById(id).ifPresentOrElse((value) -> {
            UserAuthority u = new UserAuthority(id, userAuthority.getAuthority());
            log.info("Modifying UserAuthority {}", u);
            authorityRepository.update(u);
            invalidateCache();
        }, () -> {
            final String s = String.format("Role Id %d Name %s does not Exist", id, userAuthority.getAuthority());
            log.error(s);
            throw new AuthorityDoesNotExistException(s);
        });
    }

    private void invalidateCache() {
        log.info("Invalidating Role / UserAuthority Cache");
        final Cache roleCache = cacheManager.getCache(RedisConfig.AUTHORITY_CACHE_NAME);
        if (roleCache != null) roleCache.invalidate();
    }
}
