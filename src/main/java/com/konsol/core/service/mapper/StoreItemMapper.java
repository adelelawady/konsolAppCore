package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.service.dto.ItemDTO;
import com.konsol.core.service.dto.StoreItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoreItem} and its DTO {@link StoreItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoreItemMapper extends EntityMapper<StoreItemDTO, StoreItem> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    StoreItemDTO toDto(StoreItem s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);
}
