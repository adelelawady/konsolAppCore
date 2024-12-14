package com.konsol.core.service.mapper;

import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.service.api.dto.PsSessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayStationSession} and its DTO {@link PsSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayStationSessionMapper extends EntityMapper<PsSessionDTO, PlayStationSession> {}
