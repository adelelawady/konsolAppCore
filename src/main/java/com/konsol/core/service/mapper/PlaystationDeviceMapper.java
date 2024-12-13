package com.konsol.core.service.mapper;

import com.konsol.core.domain.PlaystationDevice;
import com.konsol.core.service.dto.PlaystationDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationDevice} and its DTO {@link PlaystationDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaystationDeviceMapper extends EntityMapper<PlaystationDeviceDTO, PlaystationDevice> {}
