package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.Store;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
import com.konsol.core.service.api.dto.StoreItemDTO;
import com.konsol.core.service.api.dto.StoreItemIdOnlyDTO;
import com.konsol.core.service.api.dto.StoreNameDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:03+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class StoreItemMapperImpl implements StoreItemMapper {

    @Autowired
    private UtilitsMapper utilitsMapper;

    @Override
    public StoreItem toEntity(StoreItemDTO dto) {
        if (dto == null) {
            return null;
        }

        StoreItem storeItem = new StoreItem();

        storeItem.setId(dto.getId());
        storeItem.setQty(dto.getQty());
        storeItem.item(itemSimpleDTOToItem(dto.getItem()));
        storeItem.setStore(storeNameDTOToStore(dto.getStore()));

        return storeItem;
    }

    @Override
    public List<StoreItem> toEntity(List<StoreItemDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<StoreItem> list = new ArrayList<StoreItem>(dtoList.size());
        for (StoreItemDTO storeItemDTO : dtoList) {
            list.add(toEntity(storeItemDTO));
        }

        return list;
    }

    @Override
    public List<StoreItemDTO> toDto(List<StoreItem> entityList) {
        if (entityList == null) {
            return null;
        }

        List<StoreItemDTO> list = new ArrayList<StoreItemDTO>(entityList.size());
        for (StoreItem storeItem : entityList) {
            list.add(toDto(storeItem));
        }

        return list;
    }

    @Override
    public void partialUpdate(StoreItem entity, StoreItemDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getQty() != null) {
            entity.setQty(dto.getQty());
        }
        if (dto.getItem() != null) {
            if (entity.getItem() == null) {
                entity.item(new Item());
            }
            itemSimpleDTOToItem1(dto.getItem(), entity.getItem());
        }
        if (dto.getStore() != null) {
            if (entity.getStore() == null) {
                entity.setStore(new Store());
            }
            storeNameDTOToStore1(dto.getStore(), entity.getStore());
        }
    }

    @Override
    public StoreItemDTO toDto(StoreItem s) {
        if (s == null) {
            return null;
        }

        StoreItemDTO storeItemDTO = new StoreItemDTO();

        storeItemDTO.setId(s.getId());
        storeItemDTO.setQty(s.getQty());
        storeItemDTO.setItem(itemToItemSimpleDTO(s.getItem()));
        storeItemDTO.setStore(storeToStoreNameDTO(s.getStore()));

        return storeItemDTO;
    }

    @Override
    public StoreItemIdOnlyDTO toStoreItemIdOnlyDTO(StoreItemDTO storeItem) {
        if (storeItem == null) {
            return null;
        }

        StoreItemIdOnlyDTO storeItemIdOnlyDTO = new StoreItemIdOnlyDTO();

        storeItemIdOnlyDTO.setItemId(storeItemItemId(storeItem));
        storeItemIdOnlyDTO.setStoreId(storeItemStoreId(storeItem));
        storeItemIdOnlyDTO.setQty(storeItem.getQty());

        return storeItemIdOnlyDTO;
    }

    protected Item itemSimpleDTOToItem(ItemSimpleDTO itemSimpleDTO) {
        if (itemSimpleDTO == null) {
            return null;
        }

        Item item = new Item();

        item.setId(itemSimpleDTO.getId());
        item.setPk(utilitsMapper.intToString(itemSimpleDTO.getPk()));
        item.setName(itemSimpleDTO.getName());
        item.setPrice1(itemSimpleDTO.getPrice1());
        item.setCategory(itemSimpleDTO.getCategory());
        item.setQty(itemSimpleDTO.getQty());
        item.setCost(itemSimpleDTO.getCost());

        return item;
    }

    protected Store storeNameDTOToStore(StoreNameDTO storeNameDTO) {
        if (storeNameDTO == null) {
            return null;
        }

        Store store = new Store();

        store.setId(storeNameDTO.getId());
        store.setName(storeNameDTO.getName());

        return store;
    }

    protected void itemSimpleDTOToItem1(ItemSimpleDTO itemSimpleDTO, Item mappingTarget) {
        if (itemSimpleDTO == null) {
            return;
        }

        if (itemSimpleDTO.getId() != null) {
            mappingTarget.setId(itemSimpleDTO.getId());
        }
        if (itemSimpleDTO.getPk() != null) {
            mappingTarget.setPk(utilitsMapper.intToString(itemSimpleDTO.getPk()));
        }
        if (itemSimpleDTO.getName() != null) {
            mappingTarget.setName(itemSimpleDTO.getName());
        }
        if (itemSimpleDTO.getPrice1() != null) {
            mappingTarget.setPrice1(itemSimpleDTO.getPrice1());
        }
        if (itemSimpleDTO.getCategory() != null) {
            mappingTarget.setCategory(itemSimpleDTO.getCategory());
        }
        if (itemSimpleDTO.getQty() != null) {
            mappingTarget.setQty(itemSimpleDTO.getQty());
        }
        if (itemSimpleDTO.getCost() != null) {
            mappingTarget.setCost(itemSimpleDTO.getCost());
        }
    }

    protected void storeNameDTOToStore1(StoreNameDTO storeNameDTO, Store mappingTarget) {
        if (storeNameDTO == null) {
            return;
        }

        if (storeNameDTO.getId() != null) {
            mappingTarget.setId(storeNameDTO.getId());
        }
        if (storeNameDTO.getName() != null) {
            mappingTarget.setName(storeNameDTO.getName());
        }
    }

    protected ItemSimpleDTO itemToItemSimpleDTO(Item item) {
        if (item == null) {
            return null;
        }

        ItemSimpleDTO itemSimpleDTO = new ItemSimpleDTO();

        itemSimpleDTO.setId(item.getId());
        if (item.getPk() != null) {
            itemSimpleDTO.setPk(Integer.parseInt(item.getPk()));
        }
        itemSimpleDTO.setName(item.getName());
        itemSimpleDTO.setPrice1(item.getPrice1());
        itemSimpleDTO.setCategory(item.getCategory());
        itemSimpleDTO.setQty(item.getQty());
        itemSimpleDTO.setCost(item.getCost());

        return itemSimpleDTO;
    }

    protected StoreNameDTO storeToStoreNameDTO(Store store) {
        if (store == null) {
            return null;
        }

        StoreNameDTO storeNameDTO = new StoreNameDTO();

        storeNameDTO.setId(store.getId());
        storeNameDTO.setName(store.getName());

        return storeNameDTO;
    }

    private String storeItemItemId(StoreItemDTO storeItemDTO) {
        if (storeItemDTO == null) {
            return null;
        }
        ItemSimpleDTO item = storeItemDTO.getItem();
        if (item == null) {
            return null;
        }
        String id = item.getId();
        if (id == null) {
            return null;
        }
        return id;
    }

    private String storeItemStoreId(StoreItemDTO storeItemDTO) {
        if (storeItemDTO == null) {
            return null;
        }
        StoreNameDTO store = storeItemDTO.getStore();
        if (store == null) {
            return null;
        }
        String id = store.getId();
        if (id == null) {
            return null;
        }
        return id;
    }
}
