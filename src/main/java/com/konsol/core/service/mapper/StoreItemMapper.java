package com.konsol.core.service.mapper;

import com.konsol.core.domain.StoreItem;
import com.konsol.core.service.api.dto.StoreItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoreItem} and its DTO {@link StoreItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface StoreItemMapper extends EntityMapper<StoreItemDTO, StoreItem> {
    StoreItemDTO toDto(StoreItem s);
}
