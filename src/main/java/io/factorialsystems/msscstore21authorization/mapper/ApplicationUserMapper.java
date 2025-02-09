package io.factorialsystems.msscstore21authorization.mapper;

import io.factorialsystems.msscstore21authorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscstore21authorization.model.ApplicationUser;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
@DecoratedWith(ApplicationUserMapperDecorator.class)
public interface ApplicationUserMapper {

    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "enabled", target = "enabled"),
            @Mapping(source = "locked", target = "locked"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "authorities", ignore = true)
    })
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDto);

    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "enabled", target = "enabled"),
            @Mapping(source = "locked", target = "locked"),
            @Mapping(source = "id", target = "id"),
    })
    ApplicationUserDTO toDtoSlim(ApplicationUser applicationUser);

    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "enabled", target = "enabled"),
            @Mapping(source = "locked", target = "locked"),
            @Mapping(source = "id", target = "id"),
    })
    ApplicationUserDTO toDtoFat(ApplicationUser applicationUser);
}