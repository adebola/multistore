package io.factorialsystems.msscstore21authorization.mapper;

import io.factorialsystems.msscstore21authorization.dto.TenantRequestDTO;
import io.factorialsystems.msscstore21authorization.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21authorization.model.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface TenantMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
    })
    Tenant toEntity(TenantRequestDTO tenant);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "createdBy", source = "createdBy"),
    })
    TenantResponseDTO toDto(Tenant tenant);
}
