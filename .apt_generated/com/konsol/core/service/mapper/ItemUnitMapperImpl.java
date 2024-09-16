package com.konsol.core.service.mapper;

import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:02+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class ItemUnitMapperImpl implements ItemUnitMapper {

    @Override
    public ItemUnit toEntity(ItemUnitDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemUnit itemUnit = new ItemUnit();

        itemUnit.setId(dto.getId());
        itemUnit.setName(dto.getName());
        itemUnit.setCost(dto.getCost());
        if (dto.getBasic() != null) {
            itemUnit.setBasic(dto.getBasic());
        }
        itemUnit.setPieces(dto.getPieces());
        itemUnit.setPrice(dto.getPrice());

        return itemUnit;
    }

    @Override
    public ItemUnitDTO toDto(ItemUnit entity) {
        if (entity == null) {
            return null;
        }

        ItemUnitDTO itemUnitDTO = new ItemUnitDTO();

        itemUnitDTO.setId(entity.getId());
        itemUnitDTO.setName(entity.getName());
        itemUnitDTO.setPieces(entity.getPieces());
        itemUnitDTO.setPrice(entity.getPrice());
        itemUnitDTO.setBasic(entity.isBasic());
        itemUnitDTO.setCost(entity.getCost());

        return itemUnitDTO;
    }

    @Override
    public List<ItemUnit> toEntity(List<ItemUnitDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<ItemUnit> list = new ArrayList<ItemUnit>(dtoList.size());
        for (ItemUnitDTO itemUnitDTO : dtoList) {
            list.add(toEntity(itemUnitDTO));
        }

        return list;
    }

    @Override
    public List<ItemUnitDTO> toDto(List<ItemUnit> entityList) {
        if (entityList == null) {
            return null;
        }

        List<ItemUnitDTO> list = new ArrayList<ItemUnitDTO>(entityList.size());
        for (ItemUnit itemUnit : entityList) {
            list.add(toDto(itemUnit));
        }

        return list;
    }

    @Override
    public void partialUpdate(ItemUnit entity, ItemUnitDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getCost() != null) {
            entity.setCost(dto.getCost());
        }
        if (dto.getBasic() != null) {
            entity.setBasic(dto.getBasic());
        }
        if (dto.getPieces() != null) {
            entity.setPieces(dto.getPieces());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
    }
}
