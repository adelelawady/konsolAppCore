package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.enumeration.AccountKind;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.service.api.dto.AccountUserDTO;
import com.konsol.core.service.api.dto.BankDTO;
import com.konsol.core.service.api.dto.InvoiceDTO;
import com.konsol.core.service.api.dto.InvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceUpdateDTO;
import com.konsol.core.service.api.dto.InvoiceViewSimpleDTO;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:03+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Autowired
    private UtilitsMapper utilitsMapper;

    @Override
    public List<Invoice> toEntity(List<InvoiceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Invoice> list = new ArrayList<Invoice>( dtoList.size() );
        for ( InvoiceDTO invoiceDTO : dtoList ) {
            list.add( toEntity( invoiceDTO ) );
        }

        return list;
    }

    @Override
    public List<InvoiceDTO> toDto(List<Invoice> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<InvoiceDTO> list = new ArrayList<InvoiceDTO>( entityList.size() );
        for ( Invoice invoice : entityList ) {
            list.add( toDto( invoice ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Invoice entity, InvoiceDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedBy() != null ) {
            entity.setCreatedBy( dto.getCreatedBy() );
        }
        if ( dto.getCreatedDate() != null ) {
            entity.setCreatedDate( utilitsMapper.map( dto.getCreatedDate() ) );
        }
        if ( dto.getPk() != null ) {
            entity.setPk( dto.getPk() );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getKind() != null ) {
            entity.setKind( kindEnumToInvoiceKind( dto.getKind() ) );
        }
        if ( dto.getTotalCost() != null ) {
            entity.setTotalCost( dto.getTotalCost() );
        }
        if ( dto.getTotalPrice() != null ) {
            entity.setTotalPrice( dto.getTotalPrice() );
        }
        if ( dto.getDiscountPer() != null ) {
            entity.setDiscountPer( dto.getDiscountPer() );
        }
        if ( dto.getDiscount() != null ) {
            entity.setDiscount( dto.getDiscount() );
        }
        if ( dto.getAdditions() != null ) {
            entity.setAdditions( dto.getAdditions() );
        }
        if ( dto.getAdditionsType() != null ) {
            entity.setAdditionsType( dto.getAdditionsType() );
        }
        if ( dto.getNetCost() != null ) {
            entity.setNetCost( dto.getNetCost() );
        }
        if ( dto.getNetPrice() != null ) {
            entity.setNetPrice( dto.getNetPrice() );
        }
        if ( dto.getExpenses() != null ) {
            entity.setExpenses( dto.getExpenses() );
        }
        if ( dto.getExpensesType() != null ) {
            entity.setExpensesType( dto.getExpensesType() );
        }
        if ( dto.getBank() != null ) {
            if ( entity.getBank() == null ) {
                entity.bank( new Bank() );
            }
            bankDTOToBank( dto.getBank(), entity.getBank() );
        }
        if ( dto.getAccount() != null ) {
            if ( entity.getAccount() == null ) {
                entity.account( new AccountUser() );
            }
            accountUserDTOToAccountUser( dto.getAccount(), entity.getAccount() );
        }
        if ( entity.getInvoiceItems() != null ) {
            Set<InvoiceItem> set = invoiceItemDTOSetToInvoiceItemSet( dto.getInvoiceItems() );
            if ( set != null ) {
                entity.getInvoiceItems().clear();
                entity.getInvoiceItems().addAll( set );
            }
        }
        else {
            Set<InvoiceItem> set = invoiceItemDTOSetToInvoiceItemSet( dto.getInvoiceItems() );
            if ( set != null ) {
                entity.invoiceItems( set );
            }
        }
        if ( dto.getNetResult() != null ) {
            entity.setNetResult( dto.getNetResult() );
        }
    }

    @Override
    public InvoiceDTO toDto(Invoice s) {
        if ( s == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        invoiceDTO.setPk( s.getPk() );
        invoiceDTO.setId( s.getId() );
        invoiceDTO.setKind( invoiceKindToKindEnum( s.getKind() ) );
        invoiceDTO.setTotalCost( s.getTotalCost() );
        invoiceDTO.setTotalPrice( s.getTotalPrice() );
        invoiceDTO.setDiscountPer( s.getDiscountPer() );
        invoiceDTO.setDiscount( s.getDiscount() );
        invoiceDTO.setAdditions( s.getAdditions() );
        invoiceDTO.setAdditionsType( s.getAdditionsType() );
        invoiceDTO.setNetCost( s.getNetCost() );
        invoiceDTO.setNetPrice( s.getNetPrice() );
        invoiceDTO.setNetResult( s.getNetResult() );
        invoiceDTO.setExpenses( s.getExpenses() );
        invoiceDTO.setExpensesType( s.getExpensesType() );
        invoiceDTO.setBank( bankToBankDTO( s.getBank() ) );
        invoiceDTO.setAccount( accountUserToAccountUserDTO( s.getAccount() ) );
        invoiceDTO.setInvoiceItems( invoiceItemSetToInvoiceItemDTOSet( s.getInvoiceItems() ) );
        invoiceDTO.setCreatedBy( s.getCreatedBy() );
        invoiceDTO.setCreatedDate( utilitsMapper.map( s.getCreatedDate() ) );

        return invoiceDTO;
    }

    @Override
    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        if ( invoiceDTO == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

        invoice.setCreatedBy( invoiceDTO.getCreatedBy() );
        invoice.setCreatedDate( utilitsMapper.map( invoiceDTO.getCreatedDate() ) );
        invoice.setPk( invoiceDTO.getPk() );
        invoice.setId( invoiceDTO.getId() );
        invoice.setKind( kindEnumToInvoiceKind( invoiceDTO.getKind() ) );
        invoice.setTotalCost( invoiceDTO.getTotalCost() );
        invoice.setTotalPrice( invoiceDTO.getTotalPrice() );
        invoice.setDiscountPer( invoiceDTO.getDiscountPer() );
        invoice.setDiscount( invoiceDTO.getDiscount() );
        invoice.setAdditions( invoiceDTO.getAdditions() );
        invoice.setAdditionsType( invoiceDTO.getAdditionsType() );
        invoice.setNetCost( invoiceDTO.getNetCost() );
        invoice.setNetPrice( invoiceDTO.getNetPrice() );
        invoice.setExpenses( invoiceDTO.getExpenses() );
        invoice.setExpensesType( invoiceDTO.getExpensesType() );
        invoice.bank( bankDTOToBank1( invoiceDTO.getBank() ) );
        invoice.account( accountUserDTOToAccountUser1( invoiceDTO.getAccount() ) );
        invoice.invoiceItems( invoiceItemDTOSetToInvoiceItemSet( invoiceDTO.getInvoiceItems() ) );
        invoice.setNetResult( invoiceDTO.getNetResult() );

        return invoice;
    }

    @Override
    public void partialInvoiceUpdate(Invoice entity, InvoiceUpdateDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedDate() != null ) {
            entity.setCreatedDate( utilitsMapper.map( dto.getCreatedDate() ) );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getDiscountPer() != null ) {
            entity.setDiscountPer( dto.getDiscountPer() );
        }
        if ( dto.getDiscount() != null ) {
            entity.setDiscount( dto.getDiscount() );
        }
        if ( dto.getAdditions() != null ) {
            entity.setAdditions( dto.getAdditions() );
        }
        if ( dto.getAdditionsType() != null ) {
            entity.setAdditionsType( dto.getAdditionsType() );
        }
        if ( dto.getExpenses() != null ) {
            entity.setExpenses( dto.getExpenses() );
        }
        if ( dto.getExpensesType() != null ) {
            entity.setExpensesType( dto.getExpensesType() );
        }
    }

    @Override
    public InvoiceViewSimpleDTO toInvoiceViewSimpleDTO(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        InvoiceViewSimpleDTO invoiceViewSimpleDTO = new InvoiceViewSimpleDTO();

        invoiceViewSimpleDTO.setPk( invoice.getPk() );
        invoiceViewSimpleDTO.setId( invoice.getId() );
        invoiceViewSimpleDTO.setKind( invoiceKindToKindEnum1( invoice.getKind() ) );
        invoiceViewSimpleDTO.setTotalCost( invoice.getTotalCost() );
        invoiceViewSimpleDTO.setTotalPrice( invoice.getTotalPrice() );
        invoiceViewSimpleDTO.setDiscountPer( invoice.getDiscountPer() );
        invoiceViewSimpleDTO.setDiscount( invoice.getDiscount() );
        invoiceViewSimpleDTO.setAdditions( invoice.getAdditions() );
        invoiceViewSimpleDTO.setAdditionsType( invoice.getAdditionsType() );
        invoiceViewSimpleDTO.setNetPrice( invoice.getNetPrice() );
        invoiceViewSimpleDTO.setNetCost( invoice.getNetCost() );
        invoiceViewSimpleDTO.setNetResult( invoice.getNetResult() );
        invoiceViewSimpleDTO.setCreatedBy( invoice.getCreatedBy() );
        invoiceViewSimpleDTO.setCreatedDate( utilitsMapper.map( invoice.getCreatedDate() ) );
        invoiceViewSimpleDTO.setLastModifiedBy( invoice.getLastModifiedBy() );
        invoiceViewSimpleDTO.setLastModifiedDate( utilitsMapper.map( invoice.getLastModifiedDate() ) );
        invoiceViewSimpleDTO.setBank( bankToBankDTO( invoice.getBank() ) );
        invoiceViewSimpleDTO.setAccount( accountUserToAccountUserDTO( invoice.getAccount() ) );

        invoiceViewSimpleDTO.setItemsCount( invoice.getInvoiceItems().size()+"" );

        return invoiceViewSimpleDTO;
    }

    protected InvoiceKind kindEnumToInvoiceKind(InvoiceDTO.KindEnum kindEnum) {
        if ( kindEnum == null ) {
            return null;
        }

        InvoiceKind invoiceKind;

        switch ( kindEnum ) {
            case SALE: invoiceKind = InvoiceKind.SALE;
            break;
            case PURCHASE: invoiceKind = InvoiceKind.PURCHASE;
            break;
            case ADJUST: invoiceKind = InvoiceKind.ADJUST;
            break;
            case TRANSFER: invoiceKind = InvoiceKind.TRANSFER;
            break;
            case SALEQUOTE: invoiceKind = InvoiceKind.SALEQUOTE;
            break;
            case RETURNPUR: invoiceKind = InvoiceKind.RETURNPUR;
            break;
            case RETURNSALE: invoiceKind = InvoiceKind.RETURNSALE;
            break;
            case COMPONENT: invoiceKind = InvoiceKind.COMPONENT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + kindEnum );
        }

        return invoiceKind;
    }

    protected void bankDTOToBank(BankDTO bankDTO, Bank mappingTarget) {
        if ( bankDTO == null ) {
            return;
        }

        if ( bankDTO.getId() != null ) {
            mappingTarget.setId( bankDTO.getId() );
        }
        if ( bankDTO.getName() != null ) {
            mappingTarget.setName( bankDTO.getName() );
        }
    }

    protected AccountKind kindEnumToAccountKind(AccountUserDTO.KindEnum kindEnum) {
        if ( kindEnum == null ) {
            return null;
        }

        AccountKind accountKind;

        switch ( kindEnum ) {
            case CUSTOMER: accountKind = AccountKind.CUSTOMER;
            break;
            case SUPPLIER: accountKind = AccountKind.SUPPLIER;
            break;
            case SALEMAN: accountKind = AccountKind.SALEMAN;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + kindEnum );
        }

        return accountKind;
    }

    protected void accountUserDTOToAccountUser(AccountUserDTO accountUserDTO, AccountUser mappingTarget) {
        if ( accountUserDTO == null ) {
            return;
        }

        if ( accountUserDTO.getId() != null ) {
            mappingTarget.setId( accountUserDTO.getId() );
        }
        if ( accountUserDTO.getName() != null ) {
            mappingTarget.setName( accountUserDTO.getName() );
        }
        if ( accountUserDTO.getKind() != null ) {
            mappingTarget.setKind( kindEnumToAccountKind( accountUserDTO.getKind() ) );
        }
        if ( accountUserDTO.getBalanceIn() != null ) {
            mappingTarget.setBalanceIn( accountUserDTO.getBalanceIn() );
        }
        if ( accountUserDTO.getBalanceOut() != null ) {
            mappingTarget.setBalanceOut( accountUserDTO.getBalanceOut() );
        }
        if ( accountUserDTO.getPhone() != null ) {
            mappingTarget.setPhone( accountUserDTO.getPhone() );
        }
        if ( accountUserDTO.getAddress() != null ) {
            mappingTarget.setAddress( accountUserDTO.getAddress() );
        }
        if ( accountUserDTO.getAddress2() != null ) {
            mappingTarget.setAddress2( accountUserDTO.getAddress2() );
        }
    }

    protected Item itemSimpleDTOToItem(ItemSimpleDTO itemSimpleDTO) {
        if ( itemSimpleDTO == null ) {
            return null;
        }

        Item item = new Item();

        item.setId( itemSimpleDTO.getId() );
        item.setPk( utilitsMapper.intToString( itemSimpleDTO.getPk() ) );
        item.setName( itemSimpleDTO.getName() );
        item.setPrice1( itemSimpleDTO.getPrice1() );
        item.setCategory( itemSimpleDTO.getCategory() );
        item.setQty( itemSimpleDTO.getQty() );
        item.setCost( itemSimpleDTO.getCost() );

        return item;
    }

    protected InvoiceItem invoiceItemDTOToInvoiceItem(InvoiceItemDTO invoiceItemDTO) {
        if ( invoiceItemDTO == null ) {
            return null;
        }

        InvoiceItem invoiceItem = new InvoiceItem();

        invoiceItem.setItem( itemSimpleDTOToItem( invoiceItemDTO.getItem() ) );
        invoiceItem.setPk( invoiceItemDTO.getPk() );
        invoiceItem.setId( invoiceItemDTO.getId() );
        invoiceItem.setUnit( invoiceItemDTO.getUnit() );
        invoiceItem.setUnitPieces( invoiceItemDTO.getUnitPieces() );
        invoiceItem.setUserQty( invoiceItemDTO.getUserQty() );
        invoiceItem.setUnitQtyIn( invoiceItemDTO.getUnitQtyIn() );
        invoiceItem.setUnitQtyOut( invoiceItemDTO.getUnitQtyOut() );
        invoiceItem.setUnitCost( invoiceItemDTO.getUnitCost() );
        invoiceItem.setUnitPrice( invoiceItemDTO.getUnitPrice() );
        invoiceItem.setDiscountPer( invoiceItemDTO.getDiscountPer() );
        invoiceItem.setDiscount( invoiceItemDTO.getDiscount() );
        invoiceItem.setTotalCost( invoiceItemDTO.getTotalCost() );
        invoiceItem.setTotalPrice( invoiceItemDTO.getTotalPrice() );
        invoiceItem.setQtyIn( invoiceItemDTO.getQtyIn() );
        invoiceItem.setQtyOut( invoiceItemDTO.getQtyOut() );
        invoiceItem.setCost( invoiceItemDTO.getCost() );
        invoiceItem.setPrice( invoiceItemDTO.getPrice() );
        invoiceItem.setNetCost( invoiceItemDTO.getNetCost() );
        invoiceItem.setNetPrice( invoiceItemDTO.getNetPrice() );

        return invoiceItem;
    }

    protected Set<InvoiceItem> invoiceItemDTOSetToInvoiceItemSet(Set<InvoiceItemDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<InvoiceItem> set1 = new LinkedHashSet<InvoiceItem>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( InvoiceItemDTO invoiceItemDTO : set ) {
            set1.add( invoiceItemDTOToInvoiceItem( invoiceItemDTO ) );
        }

        return set1;
    }

    protected InvoiceDTO.KindEnum invoiceKindToKindEnum(InvoiceKind invoiceKind) {
        if ( invoiceKind == null ) {
            return null;
        }

        InvoiceDTO.KindEnum kindEnum;

        switch ( invoiceKind ) {
            case SALE: kindEnum = InvoiceDTO.KindEnum.SALE;
            break;
            case PURCHASE: kindEnum = InvoiceDTO.KindEnum.PURCHASE;
            break;
            case ADJUST: kindEnum = InvoiceDTO.KindEnum.ADJUST;
            break;
            case TRANSFER: kindEnum = InvoiceDTO.KindEnum.TRANSFER;
            break;
            case SALEQUOTE: kindEnum = InvoiceDTO.KindEnum.SALEQUOTE;
            break;
            case RETURNPUR: kindEnum = InvoiceDTO.KindEnum.RETURNPUR;
            break;
            case RETURNSALE: kindEnum = InvoiceDTO.KindEnum.RETURNSALE;
            break;
            case COMPONENT: kindEnum = InvoiceDTO.KindEnum.COMPONENT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + invoiceKind );
        }

        return kindEnum;
    }

    protected BankDTO bankToBankDTO(Bank bank) {
        if ( bank == null ) {
            return null;
        }

        BankDTO bankDTO = new BankDTO();

        bankDTO.setId( bank.getId() );
        bankDTO.setName( bank.getName() );

        return bankDTO;
    }

    protected AccountUserDTO.KindEnum accountKindToKindEnum(AccountKind accountKind) {
        if ( accountKind == null ) {
            return null;
        }

        AccountUserDTO.KindEnum kindEnum;

        switch ( accountKind ) {
            case CUSTOMER: kindEnum = AccountUserDTO.KindEnum.CUSTOMER;
            break;
            case SUPPLIER: kindEnum = AccountUserDTO.KindEnum.SUPPLIER;
            break;
            case SALEMAN: kindEnum = AccountUserDTO.KindEnum.SALEMAN;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountKind );
        }

        return kindEnum;
    }

    protected AccountUserDTO accountUserToAccountUserDTO(AccountUser accountUser) {
        if ( accountUser == null ) {
            return null;
        }

        AccountUserDTO accountUserDTO = new AccountUserDTO();

        accountUserDTO.setId( accountUser.getId() );
        accountUserDTO.setName( accountUser.getName() );
        accountUserDTO.setKind( accountKindToKindEnum( accountUser.getKind() ) );
        accountUserDTO.setBalanceIn( accountUser.getBalanceIn() );
        accountUserDTO.setBalanceOut( accountUser.getBalanceOut() );
        accountUserDTO.setPhone( accountUser.getPhone() );
        accountUserDTO.setAddress( accountUser.getAddress() );
        accountUserDTO.setAddress2( accountUser.getAddress2() );

        return accountUserDTO;
    }

    protected ItemSimpleDTO itemToItemSimpleDTO(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemSimpleDTO itemSimpleDTO = new ItemSimpleDTO();

        itemSimpleDTO.setId( item.getId() );
        if ( item.getPk() != null ) {
            itemSimpleDTO.setPk( Integer.parseInt( item.getPk() ) );
        }
        itemSimpleDTO.setName( item.getName() );
        itemSimpleDTO.setPrice1( item.getPrice1() );
        itemSimpleDTO.setCategory( item.getCategory() );
        itemSimpleDTO.setQty( item.getQty() );
        itemSimpleDTO.setCost( item.getCost() );

        return itemSimpleDTO;
    }

    protected InvoiceItemDTO invoiceItemToInvoiceItemDTO(InvoiceItem invoiceItem) {
        if ( invoiceItem == null ) {
            return null;
        }

        InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();

        invoiceItemDTO.setPk( invoiceItem.getPk() );
        invoiceItemDTO.setId( invoiceItem.getId() );
        invoiceItemDTO.setUnit( invoiceItem.getUnit() );
        invoiceItemDTO.setUnitPieces( invoiceItem.getUnitPieces() );
        invoiceItemDTO.setUserQty( invoiceItem.getUserQty() );
        invoiceItemDTO.setUnitQtyIn( invoiceItem.getUnitQtyIn() );
        invoiceItemDTO.setUnitQtyOut( invoiceItem.getUnitQtyOut() );
        invoiceItemDTO.setUnitCost( invoiceItem.getUnitCost() );
        invoiceItemDTO.setUnitPrice( invoiceItem.getUnitPrice() );
        invoiceItemDTO.setDiscountPer( invoiceItem.getDiscountPer() );
        invoiceItemDTO.setDiscount( invoiceItem.getDiscount() );
        invoiceItemDTO.setTotalCost( invoiceItem.getTotalCost() );
        invoiceItemDTO.setTotalPrice( invoiceItem.getTotalPrice() );
        invoiceItemDTO.setQtyIn( invoiceItem.getQtyIn() );
        invoiceItemDTO.setQtyOut( invoiceItem.getQtyOut() );
        invoiceItemDTO.setCost( invoiceItem.getCost() );
        invoiceItemDTO.setPrice( invoiceItem.getPrice() );
        invoiceItemDTO.setNetCost( invoiceItem.getNetCost() );
        invoiceItemDTO.setNetPrice( invoiceItem.getNetPrice() );
        invoiceItemDTO.setItem( itemToItemSimpleDTO( invoiceItem.getItem() ) );

        return invoiceItemDTO;
    }

    protected Set<InvoiceItemDTO> invoiceItemSetToInvoiceItemDTOSet(Set<InvoiceItem> set) {
        if ( set == null ) {
            return null;
        }

        Set<InvoiceItemDTO> set1 = new LinkedHashSet<InvoiceItemDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( InvoiceItem invoiceItem : set ) {
            set1.add( invoiceItemToInvoiceItemDTO( invoiceItem ) );
        }

        return set1;
    }

    protected Bank bankDTOToBank1(BankDTO bankDTO) {
        if ( bankDTO == null ) {
            return null;
        }

        Bank bank = new Bank();

        bank.setId( bankDTO.getId() );
        bank.setName( bankDTO.getName() );

        return bank;
    }

    protected AccountUser accountUserDTOToAccountUser1(AccountUserDTO accountUserDTO) {
        if ( accountUserDTO == null ) {
            return null;
        }

        AccountUser accountUser = new AccountUser();

        accountUser.setId( accountUserDTO.getId() );
        accountUser.setName( accountUserDTO.getName() );
        accountUser.setKind( kindEnumToAccountKind( accountUserDTO.getKind() ) );
        accountUser.setBalanceIn( accountUserDTO.getBalanceIn() );
        accountUser.setBalanceOut( accountUserDTO.getBalanceOut() );
        accountUser.setPhone( accountUserDTO.getPhone() );
        accountUser.setAddress( accountUserDTO.getAddress() );
        accountUser.setAddress2( accountUserDTO.getAddress2() );

        return accountUser;
    }

    protected InvoiceViewSimpleDTO.KindEnum invoiceKindToKindEnum1(InvoiceKind invoiceKind) {
        if ( invoiceKind == null ) {
            return null;
        }

        InvoiceViewSimpleDTO.KindEnum kindEnum;

        switch ( invoiceKind ) {
            case SALE: kindEnum = InvoiceViewSimpleDTO.KindEnum.SALE;
            break;
            case PURCHASE: kindEnum = InvoiceViewSimpleDTO.KindEnum.PURCHASE;
            break;
            case ADJUST: kindEnum = InvoiceViewSimpleDTO.KindEnum.ADJUST;
            break;
            case TRANSFER: kindEnum = InvoiceViewSimpleDTO.KindEnum.TRANSFER;
            break;
            case SALEQUOTE: kindEnum = InvoiceViewSimpleDTO.KindEnum.SALEQUOTE;
            break;
            case RETURNPUR: kindEnum = InvoiceViewSimpleDTO.KindEnum.RETURNPUR;
            break;
            case RETURNSALE: kindEnum = InvoiceViewSimpleDTO.KindEnum.RETURNSALE;
            break;
            case COMPONENT: kindEnum = InvoiceViewSimpleDTO.KindEnum.COMPONENT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + invoiceKind );
        }

        return kindEnum;
    }
}
