package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.service.api.dto.AccountUserDTO;
import com.konsol.core.service.api.dto.CreateAccountUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountUser} and its DTO {@link AccountUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface AccountUserMapper extends EntityMapper<AccountUserDTO, AccountUser> {

    AccountUser fromCreateAccountUser(CreateAccountUserDTO createAccountUserDTO);
}
