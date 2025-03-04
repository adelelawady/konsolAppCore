package com.konsol.core.service.mapper;

import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.service.api.dto.PsSessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayStationSession} and its DTO {@link PsSessionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UtilitsMapper.class, InvoiceMapper.class },
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PlayStationSessionMapper extends EntityMapper<PsSessionDTO, PlayStationSession> {
    @Override
    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.name", target = "deviceName")
    PsSessionDTO toDto(PlayStationSession entity);
}
