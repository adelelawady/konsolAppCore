package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Item;
import com.konsol.core.service.dto.AccountUserDTO;
import com.konsol.core.service.dto.BankDTO;
import com.konsol.core.service.dto.InvoiceDTO;
import com.konsol.core.service.dto.InvoiceItemDTO;
import com.konsol.core.service.dto.ItemDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "bank", source = "bank", qualifiedByName = "bankId")
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "account", source = "account", qualifiedByName = "accountUserId")
    @Mapping(target = "invoiceItems", source = "invoiceItems", qualifiedByName = "invoiceItemIdSet")
    InvoiceDTO toDto(Invoice s);

    @Mapping(target = "removeInvoiceItems", ignore = true)
    Invoice toEntity(InvoiceDTO invoiceDTO);

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

    @Named("invoiceItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoiceItemDTO toDtoInvoiceItemId(InvoiceItem invoiceItem);

    @Named("invoiceItemIdSet")
    default Set<InvoiceItemDTO> toDtoInvoiceItemIdSet(Set<InvoiceItem> invoiceItem) {
        return invoiceItem.stream().map(this::toDtoInvoiceItemId).collect(Collectors.toSet());
    }
}
