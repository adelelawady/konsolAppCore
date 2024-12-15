package com.konsol.core.service.mapper;

import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.service.api.dto.PsDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaystationDevice} and its DTO {@link PsDeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UtilitsMapper.class })
public interface PlaystationDeviceMapper extends EntityMapper<PsDeviceDTO, PlaystationDevice> {

}
