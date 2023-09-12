package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.domain.Money;
import com.konsol.core.domain.enumeration.AccountKind;
import com.konsol.core.domain.enumeration.MoneyKind;
import com.konsol.core.service.api.dto.AccountUserDTO;
import com.konsol.core.service.api.dto.BankDTO;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import com.konsol.core.service.api.dto.MoneyDTO;
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
public class MoneyMapperImpl implements MoneyMapper {

    @Autowired
    private UtilitsMapper utilitsMapper;

    @Override
    public Money toEntity(MoneyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Money money = new Money();

        money.setCreatedDate( utilitsMapper.map( dto.getCreatedDate() ) );
        money.setPk( dto.getPk() );
        money.setId( dto.getId() );
        money.setKind( kindEnumToMoneyKind( dto.getKind() ) );
        money.setMoneyIn( dto.getMoneyIn() );
        money.setMoneyOut( dto.getMoneyOut() );
        money.bank( bankDTOToBank( dto.getBank() ) );
        money.item( itemDTOToItem( dto.getItem() ) );
        money.account( accountUserDTOToAccountUser( dto.getAccount() ) );

        return money;
    }

    @Override
    public List<Money> toEntity(List<MoneyDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Money> list = new ArrayList<Money>( dtoList.size() );
        for ( MoneyDTO moneyDTO : dtoList ) {
            list.add( toEntity( moneyDTO ) );
        }

        return list;
    }

    @Override
    public List<MoneyDTO> toDto(List<Money> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MoneyDTO> list = new ArrayList<MoneyDTO>( entityList.size() );
        for ( Money money : entityList ) {
            list.add( toDto( money ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Money entity, MoneyDTO dto) {
        if ( dto == null ) {
            return;
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
            entity.setKind( kindEnumToMoneyKind( dto.getKind() ) );
        }
        if ( dto.getMoneyIn() != null ) {
            entity.setMoneyIn( dto.getMoneyIn() );
        }
        if ( dto.getMoneyOut() != null ) {
            entity.setMoneyOut( dto.getMoneyOut() );
        }
        if ( dto.getBank() != null ) {
            if ( entity.getBank() == null ) {
                entity.bank( new Bank() );
            }
            bankDTOToBank1( dto.getBank(), entity.getBank() );
        }
        if ( dto.getItem() != null ) {
            if ( entity.getItem() == null ) {
                entity.item( new Item() );
            }
            itemDTOToItem1( dto.getItem(), entity.getItem() );
        }
        if ( dto.getAccount() != null ) {
            if ( entity.getAccount() == null ) {
                entity.account( new AccountUser() );
            }
            accountUserDTOToAccountUser1( dto.getAccount(), entity.getAccount() );
        }
    }

    @Override
    public MoneyDTO toDto(Money s) {
        if ( s == null ) {
            return null;
        }

        MoneyDTO moneyDTO = new MoneyDTO();

        moneyDTO.setBank( toDtoBankId( s.getBank() ) );
        moneyDTO.setItem( toDtoItemId( s.getItem() ) );
        moneyDTO.setPk( s.getPk() );
        moneyDTO.setId( s.getId() );
        moneyDTO.setKind( moneyKindToKindEnum( s.getKind() ) );
        moneyDTO.setMoneyIn( s.getMoneyIn() );
        moneyDTO.setMoneyOut( s.getMoneyOut() );
        moneyDTO.setAccount( accountUserToAccountUserDTO( s.getAccount() ) );
        moneyDTO.setCreatedDate( utilitsMapper.map( s.getCreatedDate() ) );

        return moneyDTO;
    }

    @Override
    public BankDTO toDtoBankId(Bank bank) {
        if ( bank == null ) {
            return null;
        }

        BankDTO bankDTO = new BankDTO();

        bankDTO.setId( bank.getId() );

        return bankDTO;
    }

    @Override
    public ItemDTO toDtoItemId(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setId( item.getId() );

        return itemDTO;
    }

    protected MoneyKind kindEnumToMoneyKind(MoneyDTO.KindEnum kindEnum) {
        if ( kindEnum == null ) {
            return null;
        }

        MoneyKind moneyKind;

        switch ( kindEnum ) {
            case PAYMENT: moneyKind = MoneyKind.PAYMENT;
            break;
            case RECEIPT: moneyKind = MoneyKind.RECEIPT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + kindEnum );
        }

        return moneyKind;
    }

    protected Bank bankDTOToBank(BankDTO bankDTO) {
        if ( bankDTO == null ) {
            return null;
        }

        Bank bank = new Bank();

        bank.setId( bankDTO.getId() );
        bank.setName( bankDTO.getName() );

        return bank;
    }

    protected ItemUnit itemUnitDTOToItemUnit(ItemUnitDTO itemUnitDTO) {
        if ( itemUnitDTO == null ) {
            return null;
        }

        ItemUnit itemUnit = new ItemUnit();

        itemUnit.setId( itemUnitDTO.getId() );
        itemUnit.setName( itemUnitDTO.getName() );
        itemUnit.setCost( itemUnitDTO.getCost() );
        if ( itemUnitDTO.getBasic() != null ) {
            itemUnit.setBasic( itemUnitDTO.getBasic() );
        }
        itemUnit.setPieces( itemUnitDTO.getPieces() );
        itemUnit.setPrice( itemUnitDTO.getPrice() );

        return itemUnit;
    }

    protected Set<ItemUnit> itemUnitDTOSetToItemUnitSet(Set<ItemUnitDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<ItemUnit> set1 = new LinkedHashSet<ItemUnit>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ItemUnitDTO itemUnitDTO : set ) {
            set1.add( itemUnitDTOToItemUnit( itemUnitDTO ) );
        }

        return set1;
    }

    protected Item itemDTOToItem(ItemDTO itemDTO) {
        if ( itemDTO == null ) {
            return null;
        }

        Item item = new Item();

        item.setCreatedBy( itemDTO.getCreatedBy() );
        item.setCreatedDate( utilitsMapper.map( itemDTO.getCreatedDate() ) );
        item.setId( itemDTO.getId() );
        item.setPk( utilitsMapper.intToString( itemDTO.getPk() ) );
        item.setName( itemDTO.getName() );
        item.setBarcode( itemDTO.getBarcode() );
        item.setPrice1( itemDTO.getPrice1() );
        item.setPrice2( itemDTO.getPrice2() );
        item.setCategory( itemDTO.getCategory() );
        item.setQty( itemDTO.getQty() );
        item.setCost( itemDTO.getCost() );
        item.itemUnits( itemUnitDTOSetToItemUnitSet( itemDTO.getItemUnits() ) );

        return item;
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

    protected AccountUser accountUserDTOToAccountUser(AccountUserDTO accountUserDTO) {
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

    protected void bankDTOToBank1(BankDTO bankDTO, Bank mappingTarget) {
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

    protected void itemDTOToItem1(ItemDTO itemDTO, Item mappingTarget) {
        if ( itemDTO == null ) {
            return;
        }

        if ( itemDTO.getCreatedBy() != null ) {
            mappingTarget.setCreatedBy( itemDTO.getCreatedBy() );
        }
        if ( itemDTO.getCreatedDate() != null ) {
            mappingTarget.setCreatedDate( utilitsMapper.map( itemDTO.getCreatedDate() ) );
        }
        if ( itemDTO.getId() != null ) {
            mappingTarget.setId( itemDTO.getId() );
        }
        if ( itemDTO.getPk() != null ) {
            mappingTarget.setPk( utilitsMapper.intToString( itemDTO.getPk() ) );
        }
        if ( itemDTO.getName() != null ) {
            mappingTarget.setName( itemDTO.getName() );
        }
        if ( itemDTO.getBarcode() != null ) {
            mappingTarget.setBarcode( itemDTO.getBarcode() );
        }
        if ( itemDTO.getPrice1() != null ) {
            mappingTarget.setPrice1( itemDTO.getPrice1() );
        }
        if ( itemDTO.getPrice2() != null ) {
            mappingTarget.setPrice2( itemDTO.getPrice2() );
        }
        if ( itemDTO.getCategory() != null ) {
            mappingTarget.setCategory( itemDTO.getCategory() );
        }
        if ( itemDTO.getQty() != null ) {
            mappingTarget.setQty( itemDTO.getQty() );
        }
        if ( itemDTO.getCost() != null ) {
            mappingTarget.setCost( itemDTO.getCost() );
        }
        if ( mappingTarget.getItemUnits() != null ) {
            Set<ItemUnit> set = itemUnitDTOSetToItemUnitSet( itemDTO.getItemUnits() );
            if ( set != null ) {
                mappingTarget.getItemUnits().clear();
                mappingTarget.getItemUnits().addAll( set );
            }
        }
        else {
            Set<ItemUnit> set = itemUnitDTOSetToItemUnitSet( itemDTO.getItemUnits() );
            if ( set != null ) {
                mappingTarget.itemUnits( set );
            }
        }
    }

    protected void accountUserDTOToAccountUser1(AccountUserDTO accountUserDTO, AccountUser mappingTarget) {
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

    protected MoneyDTO.KindEnum moneyKindToKindEnum(MoneyKind moneyKind) {
        if ( moneyKind == null ) {
            return null;
        }

        MoneyDTO.KindEnum kindEnum;

        switch ( moneyKind ) {
            case PAYMENT: kindEnum = MoneyDTO.KindEnum.PAYMENT;
            break;
            case RECEIPT: kindEnum = MoneyDTO.KindEnum.RECEIPT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + moneyKind );
        }

        return kindEnum;
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
}
