package com.konsol.core.service.mapper;

import com.konsol.core.domain.Bank;
import com.konsol.core.service.api.dto.BankDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bank} and its DTO {@link BankDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankMapper extends EntityMapper<BankDTO, Bank> {}
