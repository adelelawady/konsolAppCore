package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.dto.ItemDTO;
import com.konsol.core.service.dto.ItemUnitDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "itemUnits", source = "itemUnits", qualifiedByName = "itemUnitIdSet")
    ItemDTO toDto(Item s);

    @Mapping(target = "removeItemUnits", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    @Named("itemUnitId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemUnitDTO toDtoItemUnitId(ItemUnit itemUnit);

    @Named("itemUnitIdSet")
    default Set<ItemUnitDTO> toDtoItemUnitIdSet(Set<ItemUnit> itemUnit) {
        return itemUnit.stream().map(this::toDtoItemUnitId).collect(Collectors.toSet());
    }
}
