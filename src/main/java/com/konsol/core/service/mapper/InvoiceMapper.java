package com.konsol.core.service.mapper;

import com.konsol.core.domain.Invoice;
import com.konsol.core.service.api.dto.InvoiceDTO;
import com.konsol.core.service.api.dto.InvoiceUpdateDTO;
import com.konsol.core.service.api.dto.InvoiceViewDTO;
import com.konsol.core.service.api.dto.InvoiceViewSimpleDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    InvoiceDTO toDto(Invoice s);

    Invoice toEntity(InvoiceDTO invoiceDTO);

    @Named("partialInvoiceUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialInvoiceUpdate(@MappingTarget Invoice entity, InvoiceUpdateDTO dto);

    @Mapping(target = "itemsCount", expression = "java(invoice.getInvoiceItems().size()+\"\")")
    InvoiceViewSimpleDTO toInvoiceViewSimpleDTO(Invoice invoice);
}
