package com.konsol.core.service.mapper;

import com.konsol.core.domain.Pk;
import com.konsol.core.service.dto.PkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pk} and its DTO {@link PkDTO}.
 */
@Mapper(componentModel = "spring")
public interface PkMapper extends EntityMapper<PkDTO, Pk> {}
