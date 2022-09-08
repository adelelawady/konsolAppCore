package com.konsol.core.service.mapper;

import com.konsol.core.domain.Store;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.service.dto.StoreDTO;
import com.konsol.core.service.dto.StoreItemDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {
    @Mapping(target = "items", source = "items", qualifiedByName = "storeItemIdSet")
    StoreDTO toDto(Store s);

    @Mapping(target = "removeItems", ignore = true)
    Store toEntity(StoreDTO storeDTO);

    @Named("storeItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoreItemDTO toDtoStoreItemId(StoreItem storeItem);

    @Named("storeItemIdSet")
    default Set<StoreItemDTO> toDtoStoreItemIdSet(Set<StoreItem> storeItem) {
        return storeItem.stream().map(this::toDtoStoreItemId).collect(Collectors.toSet());
    }
}
