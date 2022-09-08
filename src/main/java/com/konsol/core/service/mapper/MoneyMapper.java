package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.Money;
import com.konsol.core.service.dto.AccountUserDTO;
import com.konsol.core.service.dto.BankDTO;
import com.konsol.core.service.dto.ItemDTO;
import com.konsol.core.service.dto.MoneyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Money} and its DTO {@link MoneyDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyMapper extends EntityMapper<MoneyDTO, Money> {
    @Mapping(target = "bank", source = "bank", qualifiedByName = "bankId")
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "account", source = "account", qualifiedByName = "accountUserId")
    MoneyDTO toDto(Money s);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("accountUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountUserDTO toDtoAccountUserId(AccountUser accountUser);
}
