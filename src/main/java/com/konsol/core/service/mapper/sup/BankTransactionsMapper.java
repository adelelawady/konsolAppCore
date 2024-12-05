package com.konsol.core.service.mapper.sup;

import com.konsol.core.service.api.dto.BankTransactionsDTO;
import com.konsol.core.service.dto.BankBalanceDTO;
import com.konsol.core.service.mapper.EntityMapper;
import com.konsol.core.service.mapper.UtilitsMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface BankTransactionsMapper extends EntityMapper<BankTransactionsDTO, com.konsol.core.service.dto.BankTransactionsDTO> {
    @Override
    BankTransactionsDTO toDto(com.konsol.core.service.dto.BankTransactionsDTO entity);

    @Override
    com.konsol.core.service.dto.BankTransactionsDTO toEntity(BankTransactionsDTO dto);
}
