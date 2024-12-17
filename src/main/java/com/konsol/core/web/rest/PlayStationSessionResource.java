package com.konsol.core.web.rest;

import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.service.PlayStationSessionService;
import com.konsol.core.service.api.dto.PsSessionDTO;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link PlayStationSession}.
 */
@RestController
@RequestMapping("/api/play-station-sessions")
public class PlayStationSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlayStationSessionResource.class);

    private static final String ENTITY_NAME = "playStationSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayStationSessionService playStationSessionService;

    private final PlayStationSessionRepository playStationSessionRepository;

    public PlayStationSessionResource(
        PlayStationSessionService playStationSessionService,
        PlayStationSessionRepository playStationSessionRepository
    ) {
        this.playStationSessionService = playStationSessionService;
        this.playStationSessionRepository = playStationSessionRepository;
    }

   
}
