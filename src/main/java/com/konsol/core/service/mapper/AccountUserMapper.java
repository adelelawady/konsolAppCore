package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.service.dto.AccountUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountUser} and its DTO {@link AccountUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountUserMapper extends EntityMapper<AccountUserDTO, AccountUser> {}
