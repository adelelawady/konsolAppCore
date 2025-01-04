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

    @Override
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "assignedEmployee", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "totalprice", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "netPrice", ignore = true)
    @Mapping(target = "netCost", ignore = true)
    @Mapping(target = "netUserPrice", ignore = true)
    @Mapping(target = "totalItemsOut", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "invoicesAdditions", ignore = true)
    @Mapping(target = "invoicesExpenses", ignore = true)
    @Mapping(target = "totalinvoices", ignore = true)
    @Mapping(target = "totaldeletedItems", ignore = true)
    @Mapping(target = "totaldeletedItemsPrice", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    void partialUpdate(@MappingTarget Sheft entity, SheftDTO dto);
}
