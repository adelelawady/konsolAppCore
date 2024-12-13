package com.konsol.core.service.mapper;

import com.konsol.core.domain.PriceOption;
import com.konsol.core.service.dto.PriceOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PriceOption} and its DTO {@link PriceOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriceOptionMapper extends EntityMapper<PriceOptionDTO, PriceOption> {}
