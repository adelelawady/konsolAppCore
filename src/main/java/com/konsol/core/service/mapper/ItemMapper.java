package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import com.konsol.core.service.api.dto.ItemViewDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UtilitsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.WARN,
    unmappedSourcePolicy = ReportingPolicy.WARN
)
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    //@Mapping(target = "itemUnits", source = "itemUnits", qualifiedByName = "itemUnitIdSet")
    ItemDTO toDto(Item s);
    ItemViewDTO toViewDto(Item s);
    ItemSimpleDTO toSimpleDTO(Item s);

    @Mapping(target = "itemUnits", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Item toEntity(ItemDTO itemDTO);
}
