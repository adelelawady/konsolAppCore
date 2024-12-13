package com.konsol.core.service.mapper;

import com.konsol.core.domain.PlayStationSession;
import com.konsol.core.service.dto.PlayStationSessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayStationSession} and its DTO {@link PlayStationSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayStationSessionMapper extends EntityMapper<PlayStationSessionDTO, PlayStationSession> {}
