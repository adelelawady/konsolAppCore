package com.konsol.core.service.mapper;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
import com.konsol.core.service.api.dto.ItemUnitDTO;
import com.konsol.core.service.api.dto.ItemViewDTO;
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
public class ItemMapperImpl implements ItemMapper {

    @Autowired
    private UtilitsMapper utilitsMapper;

    @Override
    public List<Item> toEntity(List<ItemDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Item> list = new ArrayList<Item>( dtoList.size() );
        for ( ItemDTO itemDTO : dtoList ) {
            list.add( toEntity( itemDTO ) );
        }

        return list;
    }

    @Override
    public List<ItemDTO> toDto(List<Item> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ItemDTO> list = new ArrayList<ItemDTO>( entityList.size() );
        for ( Item item : entityList ) {
            list.add( toDto( item ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Item entity, ItemDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedBy() != null ) {
            entity.setCreatedBy( dto.getCreatedBy() );
        }
        if ( dto.getCreatedDate() != null ) {
            entity.setCreatedDate( utilitsMapper.map( dto.getCreatedDate() ) );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getPk() != null ) {
            entity.setPk( utilitsMapper.intToString( dto.getPk() ) );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getBarcode() != null ) {
            entity.setBarcode( dto.getBarcode() );
        }
        if ( dto.getPrice1() != null ) {
            entity.setPrice1( dto.getPrice1() );
        }
        if ( dto.getPrice2() != null ) {
            entity.setPrice2( dto.getPrice2() );
        }
        if ( dto.getCategory() != null ) {
            entity.setCategory( dto.getCategory() );
        }
        if ( dto.getQty() != null ) {
            entity.setQty( dto.getQty() );
        }
        if ( dto.getCost() != null ) {
            entity.setCost( dto.getCost() );
        }
        if ( entity.getItemUnits() != null ) {
            Set<ItemUnit> set = itemUnitDTOSetToItemUnitSet( dto.getItemUnits() );
            if ( set != null ) {
                entity.getItemUnits().clear();
                entity.getItemUnits().addAll( set );
            }
        }
        else {
            Set<ItemUnit> set = itemUnitDTOSetToItemUnitSet( dto.getItemUnits() );
            if ( set != null ) {
                entity.itemUnits( set );
            }
        }
    }

    @Override
    public ItemDTO toDto(Item s) {
        if ( s == null ) {
            return null;
        }

        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setId( s.getId() );
        if ( s.getPk() != null ) {
            itemDTO.setPk( Integer.parseInt( s.getPk() ) );
        }
        itemDTO.setName( s.getName() );
        itemDTO.setBarcode( s.getBarcode() );
        itemDTO.setPrice1( s.getPrice1() );
        itemDTO.setPrice2( s.getPrice2() );
        itemDTO.setCategory( s.getCategory() );
        itemDTO.setQty( s.getQty() );
        itemDTO.setCost( s.getCost() );
        itemDTO.setItemUnits( itemUnitSetToItemUnitDTOSet( s.getItemUnits() ) );
        itemDTO.setCreatedBy( s.getCreatedBy() );
        itemDTO.setCreatedDate( utilitsMapper.map( s.getCreatedDate() ) );

        return itemDTO;
    }

    @Override
    public ItemViewDTO toViewDto(Item s) {
        if ( s == null ) {
            return null;
        }

        ItemViewDTO itemViewDTO = new ItemViewDTO();

        itemViewDTO.setId( s.getId() );
        if ( s.getPk() != null ) {
            itemViewDTO.setPk( Integer.parseInt( s.getPk() ) );
        }
        itemViewDTO.setName( s.getName() );
        itemViewDTO.setBarcode( s.getBarcode() );
        itemViewDTO.setPrice1( s.getPrice1() );
        itemViewDTO.setPrice2( s.getPrice2() );
        itemViewDTO.setCategory( s.getCategory() );
        itemViewDTO.setQty( s.getQty() );
        itemViewDTO.setCost( s.getCost() );
        itemViewDTO.setCreatedBy( s.getCreatedBy() );
        itemViewDTO.setCreatedDate( utilitsMapper.map( s.getCreatedDate() ) );
        itemViewDTO.setLastModifiedBy( s.getLastModifiedBy() );
        itemViewDTO.setLastModifiedDate( utilitsMapper.map( s.getLastModifiedDate() ) );

        return itemViewDTO;
    }

    @Override
    public ItemSimpleDTO toSimpleDTO(Item s) {
        if ( s == null ) {
            return null;
        }

        ItemSimpleDTO itemSimpleDTO = new ItemSimpleDTO();

        itemSimpleDTO.setId( s.getId() );
        if ( s.getPk() != null ) {
            itemSimpleDTO.setPk( Integer.parseInt( s.getPk() ) );
        }
        itemSimpleDTO.setName( s.getName() );
        itemSimpleDTO.setPrice1( s.getPrice1() );
        itemSimpleDTO.setCategory( s.getCategory() );
        itemSimpleDTO.setQty( s.getQty() );
        itemSimpleDTO.setCost( s.getCost() );

        return itemSimpleDTO;
    }

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        if ( itemDTO == null ) {
            return null;
        }

        Item item = new Item();

        item.setId( itemDTO.getId() );
        item.setPk( utilitsMapper.intToString( itemDTO.getPk() ) );
        item.setName( itemDTO.getName() );
        item.setBarcode( itemDTO.getBarcode() );
        item.setPrice1( itemDTO.getPrice1() );
        item.setPrice2( itemDTO.getPrice2() );
        item.setCategory( itemDTO.getCategory() );
        item.setQty( itemDTO.getQty() );
        item.setCost( itemDTO.getCost() );

        return item;
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

    protected ItemUnitDTO itemUnitToItemUnitDTO(ItemUnit itemUnit) {
        if ( itemUnit == null ) {
            return null;
        }

        ItemUnitDTO itemUnitDTO = new ItemUnitDTO();

        itemUnitDTO.setId( itemUnit.getId() );
        itemUnitDTO.setName( itemUnit.getName() );
        itemUnitDTO.setPieces( itemUnit.getPieces() );
        itemUnitDTO.setPrice( itemUnit.getPrice() );
        itemUnitDTO.setBasic( itemUnit.isBasic() );
        itemUnitDTO.setCost( itemUnit.getCost() );

        return itemUnitDTO;
    }

    protected Set<ItemUnitDTO> itemUnitSetToItemUnitDTOSet(Set<ItemUnit> set) {
        if ( set == null ) {
            return null;
        }

        Set<ItemUnitDTO> set1 = new LinkedHashSet<ItemUnitDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ItemUnit itemUnit : set ) {
            set1.add( itemUnitToItemUnitDTO( itemUnit ) );
        }

        return set1;
    }
}
