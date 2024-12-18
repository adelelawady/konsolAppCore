package com.konsol.core.service.mapper;

import com.konsol.core.domain.PlaystationContainer;
import com.konsol.core.service.dto.PlaystationContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationContainer} and its DTO {@link PlaystationContainerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaystationContainerMapper extends EntityMapper<PlaystationContainerDTO, PlaystationContainer> {}
