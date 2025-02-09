package io.factorialsystems.msscstore21authorization.mapper;

import io.factorialsystems.msscstore21authorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscstore21authorization.model.ApplicationUser;
import io.factorialsystems.msscstore21authorization.model.UserAuthority;
import io.factorialsystems.msscstore21authorization.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ApplicationUserMapperDecorator implements ApplicationUserMapper{
    private ApplicationUserMapper mapper;
    private AuthorityRepository authorityRepository;

    @Autowired
    public void setMapper(ApplicationUserMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRepository (AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public ApplicationUser toEntity(ApplicationUserDTO applicationUserDto) {
        final Set<UserAuthority> authorities;
        final Set<String> roles = applicationUserDto.getRoles();

        ApplicationUser applicationUser = mapper.toEntity(applicationUserDto);

        if (roles == null || roles.isEmpty()) {
            authorities = Set.of();
        } else {
            final List<UserAuthority> foundAuthorities = authorityRepository.findAuthorities(roles.stream().toList());
            authorities = new HashSet<>(foundAuthorities);
        }

        applicationUser.setAuthorities(authorities);

        return applicationUser;
    }

    @Override
    public ApplicationUserDTO toDtoSlim(ApplicationUser applicationUser) {
        return mapper.toDtoSlim(applicationUser);
    }

    @Override
    public ApplicationUserDTO toDtoFat(ApplicationUser applicationUser) {
        return mapper.toDtoFat(applicationUser);
    }
}
