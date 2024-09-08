package com.konsol.core.web.rest.api;

import com.konsol.core.domain.Settings;
import com.konsol.core.repository.StoreRepository;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.StoreService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.SettingsMapper;
import com.konsol.core.web.api.StoresApi;
import com.konsol.core.web.api.StoresApiDelegate;
import com.konsol.core.web.api.SysApi;
import com.konsol.core.web.api.SysApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Store}.
 */
@Service
public class SettingsResource implements SysApiDelegate {

    private final Logger log = LoggerFactory.getLogger(SettingsResource.class);

    private static final String ENTITY_NAME = "store";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private SettingService settingService;

    @Autowired
    private SettingsMapper settingsMapper;

    @Override
    public ResponseEntity<ServerSettings> getServerSettings() {
        log.debug("REST request to get ServerSettings");
        return ResponseEntity.ok(settingsMapper.toDto(settingService.getSettings()));
    }

    @Override
    public ResponseEntity<ServerSettings> updateServerSettings(ServerSettings serverSettings) {
        log.debug("REST request to update ServerSettings : {}", serverSettings);
        return ResponseEntity.ok(settingsMapper.toDto(settingService.update(settingsMapper.toEntity(serverSettings))));
    }
}
