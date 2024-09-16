package com.konsol.core.service.mapper;

import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Item;
import com.konsol.core.service.api.dto.InvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceItemUpdateDTO;
import com.konsol.core.service.api.dto.InvoiceItemViewDTO;
import com.konsol.core.service.api.dto.ItemSimpleDTO;
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
public class InvoiceItemMapperImpl implements InvoiceItemMapper {

    @Override
    public InvoiceItem toEntity(InvoiceItemDTO dto) {
        if (dto == null) {
            return null;
        }

        InvoiceItem invoiceItem = new InvoiceItem();

        invoiceItem.setItem(itemSimpleDTOToItem(dto.getItem()));
        invoiceItem.setPk(dto.getPk());
        invoiceItem.setId(dto.getId());
        invoiceItem.setUnit(dto.getUnit());
        invoiceItem.setUnitPieces(dto.getUnitPieces());
        invoiceItem.setUserQty(dto.getUserQty());
        invoiceItem.setUnitQtyIn(dto.getUnitQtyIn());
        invoiceItem.setUnitQtyOut(dto.getUnitQtyOut());
        invoiceItem.setUnitCost(dto.getUnitCost());
        invoiceItem.setUnitPrice(dto.getUnitPrice());
        invoiceItem.setDiscountPer(dto.getDiscountPer());
        invoiceItem.setDiscount(dto.getDiscount());
        invoiceItem.setTotalCost(dto.getTotalCost());
        invoiceItem.setTotalPrice(dto.getTotalPrice());
        invoiceItem.setQtyIn(dto.getQtyIn());
        invoiceItem.setQtyOut(dto.getQtyOut());
        invoiceItem.setCost(dto.getCost());
        invoiceItem.setPrice(dto.getPrice());
        invoiceItem.setNetCost(dto.getNetCost());
        invoiceItem.setNetPrice(dto.getNetPrice());

        return invoiceItem;
    }

    @Override
    public InvoiceItemDTO toDto(InvoiceItem entity) {
        if (entity == null) {
            return null;
        }

        InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();

        invoiceItemDTO.setPk(entity.getPk());
        invoiceItemDTO.setId(entity.getId());
        invoiceItemDTO.setUnit(entity.getUnit());
        invoiceItemDTO.setUnitPieces(entity.getUnitPieces());
        invoiceItemDTO.setUserQty(entity.getUserQty());
        invoiceItemDTO.setUnitQtyIn(entity.getUnitQtyIn());
        invoiceItemDTO.setUnitQtyOut(entity.getUnitQtyOut());
        invoiceItemDTO.setUnitCost(entity.getUnitCost());
        invoiceItemDTO.setUnitPrice(entity.getUnitPrice());
        invoiceItemDTO.setDiscountPer(entity.getDiscountPer());
        invoiceItemDTO.setDiscount(entity.getDiscount());
        invoiceItemDTO.setTotalCost(entity.getTotalCost());
        invoiceItemDTO.setTotalPrice(entity.getTotalPrice());
        invoiceItemDTO.setQtyIn(entity.getQtyIn());
        invoiceItemDTO.setQtyOut(entity.getQtyOut());
        invoiceItemDTO.setCost(entity.getCost());
        invoiceItemDTO.setPrice(entity.getPrice());
        invoiceItemDTO.setNetCost(entity.getNetCost());
        invoiceItemDTO.setNetPrice(entity.getNetPrice());
        invoiceItemDTO.setItem(itemToItemSimpleDTO(entity.getItem()));

        return invoiceItemDTO;
    }

    @Override
    public List<InvoiceItem> toEntity(List<InvoiceItemDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<InvoiceItem> list = new ArrayList<InvoiceItem>(dtoList.size());
        for (InvoiceItemDTO invoiceItemDTO : dtoList) {
            list.add(toEntity(invoiceItemDTO));
        }

        return list;
    }

    @Override
    public List<InvoiceItemDTO> toDto(List<InvoiceItem> entityList) {
        if (entityList == null) {
            return null;
        }

        List<InvoiceItemDTO> list = new ArrayList<InvoiceItemDTO>(entityList.size());
        for (InvoiceItem invoiceItem : entityList) {
            list.add(toDto(invoiceItem));
        }

        return list;
    }

    @Override
    public void partialUpdate(InvoiceItem entity, InvoiceItemDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getItem() != null) {
            if (entity.getItem() == null) {
                entity.setItem(new Item());
            }
            itemSimpleDTOToItem1(dto.getItem(), entity.getItem());
        }
        if (dto.getPk() != null) {
            entity.setPk(dto.getPk());
        }
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getUnit() != null) {
            entity.setUnit(dto.getUnit());
        }
        if (dto.getUnitPieces() != null) {
            entity.setUnitPieces(dto.getUnitPieces());
        }
        if (dto.getUserQty() != null) {
            entity.setUserQty(dto.getUserQty());
        }
        if (dto.getUnitQtyIn() != null) {
            entity.setUnitQtyIn(dto.getUnitQtyIn());
        }
        if (dto.getUnitQtyOut() != null) {
            entity.setUnitQtyOut(dto.getUnitQtyOut());
        }
        if (dto.getUnitCost() != null) {
            entity.setUnitCost(dto.getUnitCost());
        }
        if (dto.getUnitPrice() != null) {
            entity.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getDiscountPer() != null) {
            entity.setDiscountPer(dto.getDiscountPer());
        }
        if (dto.getDiscount() != null) {
            entity.setDiscount(dto.getDiscount());
        }
        if (dto.getTotalCost() != null) {
            entity.setTotalCost(dto.getTotalCost());
        }
        if (dto.getTotalPrice() != null) {
            entity.setTotalPrice(dto.getTotalPrice());
        }
        if (dto.getQtyIn() != null) {
            entity.setQtyIn(dto.getQtyIn());
        }
        if (dto.getQtyOut() != null) {
            entity.setQtyOut(dto.getQtyOut());
        }
        if (dto.getCost() != null) {
            entity.setCost(dto.getCost());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getNetCost() != null) {
            entity.setNetCost(dto.getNetCost());
        }
        if (dto.getNetPrice() != null) {
            entity.setNetPrice(dto.getNetPrice());
        }
    }

    @Override
    public InvoiceItemViewDTO toInvoiceItemViewDTO(InvoiceItem invoiceItem) {
        if (invoiceItem == null) {
            return null;
        }

        InvoiceItemViewDTO invoiceItemViewDTO = new InvoiceItemViewDTO();

        invoiceItemViewDTO.setPk(invoiceItem.getPk());
        invoiceItemViewDTO.setId(invoiceItem.getId());
        invoiceItemViewDTO.setUnit(invoiceItem.getUnit());
        invoiceItemViewDTO.setUnitQtyIn(invoiceItem.getUnitQtyIn());
        invoiceItemViewDTO.setUnitQtyOut(invoiceItem.getUnitQtyOut());
        invoiceItemViewDTO.setDiscount(invoiceItem.getDiscount());
        invoiceItemViewDTO.setDiscountPer(invoiceItem.getDiscountPer());
        invoiceItemViewDTO.setTotalCost(invoiceItem.getTotalCost());
        invoiceItemViewDTO.setTotalPrice(invoiceItem.getTotalPrice());
        invoiceItemViewDTO.setQtyIn(invoiceItem.getQtyIn());
        invoiceItemViewDTO.setQtyOut(invoiceItem.getQtyOut());
        invoiceItemViewDTO.setNetCost(invoiceItem.getNetCost());
        invoiceItemViewDTO.setNetPrice(invoiceItem.getNetPrice());

        return invoiceItemViewDTO;
    }

    @Override
    public void partialUpdate(InvoiceItem entity, InvoiceItemUpdateDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getUnitPieces() != null) {
            entity.setUnitPieces(dto.getUnitPieces());
        }
        if (dto.getUnitCost() != null) {
            entity.setUnitCost(dto.getUnitCost());
        }
        if (dto.getUnitPrice() != null) {
            entity.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getDiscountPer() != null) {
            entity.setDiscountPer(dto.getDiscountPer());
        }
        if (dto.getDiscount() != null) {
            entity.setDiscount(dto.getDiscount());
        }
        if (dto.getCost() != null) {
            entity.setCost(dto.getCost());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
    }

    protected Item itemSimpleDTOToItem(ItemSimpleDTO itemSimpleDTO) {
        if (itemSimpleDTO == null) {
            return null;
        }

        Item item = new Item();

        item.setId(itemSimpleDTO.getId());
        if (itemSimpleDTO.getPk() != null) {
            item.setPk(String.valueOf(itemSimpleDTO.getPk()));
        }
        item.setName(itemSimpleDTO.getName());
        item.setPrice1(itemSimpleDTO.getPrice1());
        item.setCategory(itemSimpleDTO.getCategory());
        item.setQty(itemSimpleDTO.getQty());
        item.setCost(itemSimpleDTO.getCost());

        return item;
    }

    protected ItemSimpleDTO itemToItemSimpleDTO(Item item) {
        if (item == null) {
            return null;
        }

        ItemSimpleDTO itemSimpleDTO = new ItemSimpleDTO();

        itemSimpleDTO.setId(item.getId());
        if (item.getPk() != null) {
            itemSimpleDTO.setPk(Integer.parseInt(item.getPk()));
        }
        itemSimpleDTO.setName(item.getName());
        itemSimpleDTO.setPrice1(item.getPrice1());
        itemSimpleDTO.setCategory(item.getCategory());
        itemSimpleDTO.setQty(item.getQty());
        itemSimpleDTO.setCost(item.getCost());

        return itemSimpleDTO;
    }

    protected void itemSimpleDTOToItem1(ItemSimpleDTO itemSimpleDTO, Item mappingTarget) {
        if (itemSimpleDTO == null) {
            return;
        }

        if (itemSimpleDTO.getId() != null) {
            mappingTarget.setId(itemSimpleDTO.getId());
        }
        if (itemSimpleDTO.getPk() != null) {
            mappingTarget.setPk(String.valueOf(itemSimpleDTO.getPk()));
        }
        if (itemSimpleDTO.getName() != null) {
            mappingTarget.setName(itemSimpleDTO.getName());
        }
        if (itemSimpleDTO.getPrice1() != null) {
            mappingTarget.setPrice1(itemSimpleDTO.getPrice1());
        }
        if (itemSimpleDTO.getCategory() != null) {
            mappingTarget.setCategory(itemSimpleDTO.getCategory());
        }
        if (itemSimpleDTO.getQty() != null) {
            mappingTarget.setQty(itemSimpleDTO.getQty());
        }
        if (itemSimpleDTO.getCost() != null) {
            mappingTarget.setCost(itemSimpleDTO.getCost());
        }
    }
}
