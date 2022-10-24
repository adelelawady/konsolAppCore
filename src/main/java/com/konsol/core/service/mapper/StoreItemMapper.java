package com.konsol.core.service.mapper;

import com.konsol.core.domain.StoreItem;
import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.service.api.dto.StoreItemIdOnlyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoreItem} and its DTO {@link StoreItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface StoreItemMapper extends EntityMapper<StoreItemDTO, StoreItem> {
    StoreItemDTO toDto(StoreItem s);

    default StoreItemIdOnlyDTO toStoreItemIdOnlyDTO(StoreItem storeItem) {
        return toStoreItemIdOnlyDTO(toDto(storeItem));
    }

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "store.id", target = "storeId")
    StoreItemIdOnlyDTO toStoreItemIdOnlyDTO(StoreItemDTO storeItem);
}
