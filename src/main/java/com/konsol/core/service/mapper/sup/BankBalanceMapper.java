package com.konsol.core.service.mapper.sup;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.service.api.dto.AccountUserDTO;
import com.konsol.core.service.dto.BankBalanceDTO;
import com.konsol.core.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankBalanceMapper extends EntityMapper<com.konsol.core.service.api.dto.BankBalanceDTO, BankBalanceDTO> {
    @Override
    BankBalanceDTO toEntity(com.konsol.core.service.api.dto.BankBalanceDTO dto);

    @Override
    com.konsol.core.service.api.dto.BankBalanceDTO toDto(BankBalanceDTO entity);
}
