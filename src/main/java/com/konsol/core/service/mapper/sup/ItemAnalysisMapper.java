package com.konsol.core.service.mapper.sup;

import com.konsol.core.service.api.dto.BankTransactionsDTO;
import com.konsol.core.service.api.dto.ItemAnalysisDTO;
import com.konsol.core.service.mapper.EntityMapper;
import com.konsol.core.service.mapper.UtilitsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = { UtilitsMapper.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ItemAnalysisMapper extends EntityMapper<ItemAnalysisDTO, com.konsol.core.service.dto.ItemAnalysisDTO> {
    @Override
    ItemAnalysisDTO toDto(com.konsol.core.service.dto.ItemAnalysisDTO entity);

    @Override
    com.konsol.core.service.dto.ItemAnalysisDTO toEntity(ItemAnalysisDTO dto);
}
