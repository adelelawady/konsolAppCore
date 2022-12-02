package com.konsol.core.service.mapper;

import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.service.api.dto.InvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceItemUpdateDTO;
import com.konsol.core.service.api.dto.InvoiceItemViewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceItem} and its DTO {@link InvoiceItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceItemMapper extends EntityMapper<InvoiceItemDTO, InvoiceItem> {
    InvoiceItemViewDTO toInvoiceItemViewDTO(InvoiceItem invoiceItem);

    @Named("partialUpdateInvoiceItem")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget InvoiceItem entity, InvoiceItemUpdateDTO dto);
}
