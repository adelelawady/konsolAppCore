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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @Override
    public ResponseEntity<Boolean> performBackup() {
        log.debug("REST request to perform system backup");
        try {
            // Execute backup and wait for completion with timeout
            Boolean success = settingService.performBackup().get(30, TimeUnit.MINUTES); // 30 minute timeout

            if (success) {
                return ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createAlert(applicationName, "Backup completed successfully", settingService.getLatestBackupPath()))
                    .body(true);
            } else {
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, "backup", "backupFailed", "Backup operation failed"))
                    .body(false);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Backup operation was interrupted", e);
            return handleBackupError("Backup operation was interrupted");
        } catch (ExecutionException e) {
            log.error("Error during backup execution", e.getCause());
            return handleBackupError("Error during backup: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            log.error("Backup operation timed out", e);
            return handleBackupError("Backup operation timed out after 30 minutes");
        }
    }

    @Override
    public ResponseEntity<Boolean> performRestore(PerformRestoreRequest performRestoreRequest) {
        log.debug("REST request to restore system from backup: {}", performRestoreRequest.getBackupPath());

        if (performRestoreRequest.getBackupPath() == null || performRestoreRequest.getBackupPath().trim().isEmpty()) {
            return ResponseEntity
                .badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, "restore", "invalidPath", "Backup path cannot be empty"))
                .body(false);
        }

        try {
            // Execute restore and wait for completion with timeout
            Boolean success = settingService.performRestore(performRestoreRequest.getBackupPath()).get(30, TimeUnit.MINUTES); // 30 minute timeout

            if (success) {
                return ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createAlert(applicationName, "System restored successfully", performRestoreRequest.getBackupPath()))
                    .body(true);
            } else {
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, "restore", "restoreFailed", "Restore operation failed"))
                    .body(false);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Restore operation was interrupted", e);
            return handleRestoreError("Restore operation was interrupted");
        } catch (ExecutionException e) {
            log.error("Error during restore execution", e.getCause());
            return handleRestoreError("Error during restore: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            log.error("Restore operation timed out", e);
            return handleRestoreError("Restore operation timed out after 30 minutes");
        }
    }

    private ResponseEntity<Boolean> handleBackupError(String errorMessage) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .headers(HeaderUtil.createFailureAlert(applicationName, true, "backup", "backupError", errorMessage))
            .body(false);
    }

    private ResponseEntity<Boolean> handleRestoreError(String errorMessage) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .headers(HeaderUtil.createFailureAlert(applicationName, true, "restore", "restoreError", errorMessage))
            .body(false);
    }
}
