package com.konsol.core.service.mapper;

import com.konsol.core.domain.AccountUser;
import com.konsol.core.service.dto.AccountUserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:04+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class AccountUserMapperImpl implements AccountUserMapper {

    @Override
    public AccountUser toEntity(AccountUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AccountUser accountUser = new AccountUser();

        accountUser.setId( dto.getId() );
        accountUser.setName( dto.getName() );
        accountUser.setKind( dto.getKind() );
        accountUser.setBalanceIn( dto.getBalanceIn() );
        accountUser.setBalanceOut( dto.getBalanceOut() );
        accountUser.setPhone( dto.getPhone() );
        accountUser.setAddress( dto.getAddress() );
        accountUser.setAddress2( dto.getAddress2() );

        return accountUser;
    }

    @Override
    public AccountUserDTO toDto(AccountUser entity) {
        if ( entity == null ) {
            return null;
        }

        AccountUserDTO accountUserDTO = new AccountUserDTO();

        accountUserDTO.setId( entity.getId() );
        accountUserDTO.setName( entity.getName() );
        accountUserDTO.setKind( entity.getKind() );
        accountUserDTO.setBalanceIn( entity.getBalanceIn() );
        accountUserDTO.setBalanceOut( entity.getBalanceOut() );
        accountUserDTO.setPhone( entity.getPhone() );
        accountUserDTO.setAddress( entity.getAddress() );
        accountUserDTO.setAddress2( entity.getAddress2() );

        return accountUserDTO;
    }

    @Override
    public List<AccountUser> toEntity(List<AccountUserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AccountUser> list = new ArrayList<AccountUser>( dtoList.size() );
        for ( AccountUserDTO accountUserDTO : dtoList ) {
            list.add( toEntity( accountUserDTO ) );
        }

        return list;
    }

    @Override
    public List<AccountUserDTO> toDto(List<AccountUser> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AccountUserDTO> list = new ArrayList<AccountUserDTO>( entityList.size() );
        for ( AccountUser accountUser : entityList ) {
            list.add( toDto( accountUser ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AccountUser entity, AccountUserDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getKind() != null ) {
            entity.setKind( dto.getKind() );
        }
        if ( dto.getBalanceIn() != null ) {
            entity.setBalanceIn( dto.getBalanceIn() );
        }
        if ( dto.getBalanceOut() != null ) {
            entity.setBalanceOut( dto.getBalanceOut() );
        }
        if ( dto.getPhone() != null ) {
            entity.setPhone( dto.getPhone() );
        }
        if ( dto.getAddress() != null ) {
            entity.setAddress( dto.getAddress() );
        }
        if ( dto.getAddress2() != null ) {
            entity.setAddress2( dto.getAddress2() );
        }
    }
}
