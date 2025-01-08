package com.konsol.core.service.mapper;

import com.konsol.core.domain.PlaystationContainer;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationContainer} and its DTO {@link PlaystationContainer}.
 */
@Mapper(componentModel = "spring")
public interface PlaystationContainerMapper

    extends EntityMapper<com.konsol.core.service.api.dto.PlaystationContainer, PlaystationContainer> {}
