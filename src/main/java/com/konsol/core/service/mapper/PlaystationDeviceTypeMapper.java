package com.konsol.core.service.mapper;

import com.konsol.core.domain.PlaystationDeviceType;
import com.konsol.core.service.dto.PlaystationDeviceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationDeviceType} and its DTO {@link PlaystationDeviceTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaystationDeviceTypeMapper extends EntityMapper<PlaystationDeviceTypeDTO, PlaystationDeviceType> {}
