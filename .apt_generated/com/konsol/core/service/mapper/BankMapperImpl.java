package com.konsol.core.service.mapper;

import com.konsol.core.domain.Bank;
import com.konsol.core.service.api.dto.BankDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-06T19:03:02+0200",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.4 (Eclipse Adoptium)"
)
@Component
public class BankMapperImpl implements BankMapper {

    @Override
    public Bank toEntity(BankDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Bank bank = new Bank();

        bank.setId( dto.getId() );
        bank.setName( dto.getName() );

        return bank;
    }

    @Override
    public BankDTO toDto(Bank entity) {
        if ( entity == null ) {
            return null;
        }

        BankDTO bankDTO = new BankDTO();

        bankDTO.setId( entity.getId() );
        bankDTO.setName( entity.getName() );

        return bankDTO;
    }

    @Override
    public List<Bank> toEntity(List<BankDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Bank> list = new ArrayList<Bank>( dtoList.size() );
        for ( BankDTO bankDTO : dtoList ) {
            list.add( toEntity( bankDTO ) );
        }

        return list;
    }

    @Override
    public List<BankDTO> toDto(List<Bank> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BankDTO> list = new ArrayList<BankDTO>( entityList.size() );
        for ( Bank bank : entityList ) {
            list.add( toDto( bank ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Bank entity, BankDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
    }
}
