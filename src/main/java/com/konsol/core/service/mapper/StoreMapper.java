package com.konsol.core.service.mapper;

import com.konsol.core.domain.Store;
import com.konsol.core.service.api.dto.StoreDTO;
import com.konsol.core.service.api.dto.StoreNameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {
    StoreDTO toDto(Store s);

    Store toEntity(StoreDTO storeDTO);

    StoreNameDTO toStoreNameDTO(Store store);
}
