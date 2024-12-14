package com.konsol.core.service.mapper;

import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.service.api.dto.PsDeviceType;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationDeviceType} and its DTO {@link PsDeviceType}.
 */
@Mapper(componentModel = "spring")
public interface PlaystationDeviceTypeMapper extends EntityMapper<PsDeviceType, PlaystationDeviceType> {}
