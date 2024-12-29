package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
import com.konsol.core.service.api.dto.ItemViewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UtilitsMapper.class, ItemPriceOptionMapper.class },
    unmappedTargetPolicy = ReportingPolicy.WARN,
    unmappedSourcePolicy = ReportingPolicy.WARN
)
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    ItemDTO toDto(Item s);
    ItemViewDTO toViewDto(Item s);
    ItemSimpleDTO toSimpleDTO(Item s);

    @Mapping(target = "itemUnits", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "qty", ignore = true)
    @Mapping(target = "buildIn", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    @Override
    @Mapping(target = "itemUnits", ignore = true)
    @Mapping(target = "qty", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "buildIn", ignore = true)
    void partialUpdate(@MappingTarget Item entity, ItemDTO dto);
}
