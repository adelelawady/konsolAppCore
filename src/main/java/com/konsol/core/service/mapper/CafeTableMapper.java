package com.konsol.core.service.mapper;

import com.konsol.core.domain.CafeTable;
import com.konsol.core.service.dto.CafeTableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CafeTable} and its DTO {@link CafeTableDTO}.
 */
@Mapper(componentModel = "spring")
public interface CafeTableMapper extends EntityMapper<CafeTableDTO, CafeTable> {}
