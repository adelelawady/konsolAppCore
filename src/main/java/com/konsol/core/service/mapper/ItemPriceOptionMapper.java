package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemPriceOptions;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.api.dto.ItemPriceOptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UtilitsMapper.class },
    unmappedTargetPolicy = ReportingPolicy.WARN,
    unmappedSourcePolicy = ReportingPolicy.WARN
)
public interface ItemPriceOptionMapper extends EntityMapper<ItemPriceOptionDTO, ItemPriceOptions> {}
