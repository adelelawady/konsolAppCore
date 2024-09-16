package com.konsol.core.service.mapper;

import com.konsol.core.domain.Store;
import com.konsol.core.service.api.dto.StoreDTO;
import com.konsol.core.service.api.dto.StoreNameDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:03+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class StoreMapperImpl implements StoreMapper {

    @Override
    public List<Store> toEntity(List<StoreDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Store> list = new ArrayList<Store>(dtoList.size());
        for (StoreDTO storeDTO : dtoList) {
            list.add(toEntity(storeDTO));
        }

        return list;
    }

    @Override
    public List<StoreDTO> toDto(List<Store> entityList) {
        if (entityList == null) {
            return null;
        }

        List<StoreDTO> list = new ArrayList<StoreDTO>(entityList.size());
        for (Store store : entityList) {
            list.add(toDto(store));
        }

        return list;
    }

    @Override
    public void partialUpdate(Store entity, StoreDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
    }

    @Override
    public StoreDTO toDto(Store s) {
        if (s == null) {
            return null;
        }

        StoreDTO storeDTO = new StoreDTO();

        storeDTO.setId(s.getId());
        storeDTO.setName(s.getName());

        return storeDTO;
    }

    @Override
    public Store toEntity(StoreDTO storeDTO) {
        if (storeDTO == null) {
            return null;
        }

        Store store = new Store();

        store.setId(storeDTO.getId());
        store.setName(storeDTO.getName());

        return store;
    }

    @Override
    public StoreNameDTO toStoreNameDTO(Store store) {
        if (store == null) {
            return null;
        }

        StoreNameDTO storeNameDTO = new StoreNameDTO();

        storeNameDTO.setId(store.getId());
        storeNameDTO.setName(store.getName());

        return storeNameDTO;
    }
}
