package com.konsol.core.service.mapper;

import com.konsol.core.domain.Sheft;
import com.konsol.core.service.api.dto.SheftDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sheft} and its DTO {@link SheftDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UtilitsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface SheftMapper extends EntityMapper<SheftDTO, Sheft> {
    @Override
    SheftDTO toDto(Sheft entity);
}
