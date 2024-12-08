package com.konsol.core.service.mapper.sup;

import com.konsol.core.service.dto.AccountTransactionsDTO;
import com.konsol.core.service.dto.BankTransactionsDTO;
import com.konsol.core.service.mapper.EntityMapper;
import com.konsol.core.service.mapper.UtilitsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Mapper for Account Transactions
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccountTransactionsMapper
    extends EntityMapper<com.konsol.core.service.api.dto.AccountTransactionsDTO, com.konsol.core.service.dto.AccountTransactionsDTO> {
    @Override
    com.konsol.core.service.api.dto.AccountTransactionsDTO toDto(AccountTransactionsDTO entity);

    @Override
    AccountTransactionsDTO toEntity(com.konsol.core.service.api.dto.AccountTransactionsDTO dto);
}
