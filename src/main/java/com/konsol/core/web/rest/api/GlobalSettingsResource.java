package com.konsol.core.web.rest.api;

import com.konsol.core.repository.BankRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.BankService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.SettingsMapper;
import com.konsol.core.service.mapper.sup.BankBalanceMapper;
import com.konsol.core.service.mapper.sup.BankTransactionsMapper;
import com.konsol.core.web.api.BanksApiDelegate;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.core.domain.Bank}.
 */
@Service
public class GlobalSettingsResource implements SysApiDelegate {

    private final Logger log = LoggerFactory.getLogger(GlobalSettingsResource.class);

    private static final String ENTITY_NAME = "bank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingsMapper settingsMapper;

    private final SettingService settingService;

    public GlobalSettingsResource(SettingsMapper settingsMapper, SettingService settingService) {
        this.settingsMapper = settingsMapper;

        this.settingService = settingService;
    }

    @Override
    public ResponseEntity<ServerSettings> getServerSettings() {
        return ResponseEntity.ok(settingsMapper.toDto(settingService.getSettings()));
    }

    @Override
    public ResponseEntity<ServerSettings> updateServerSettings(ServerSettings serverSettings) {
        log.debug("REST request to update Settings : {}", serverSettings);
        if (serverSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServerSettings result = settingsMapper.toDto(settingService.update(settingsMapper.toEntity(serverSettings)));
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serverSettings.getId().toString()))
            .body(result);
    }
}
