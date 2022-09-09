package com.konsol.core.service.mapper;

import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemUnit} and its DTO {@link ItemUnitDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemUnitMapper extends EntityMapper<ItemUnitDTO, ItemUnit> {}
