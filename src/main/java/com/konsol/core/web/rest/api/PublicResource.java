package com.konsol.core.web.rest.api;

import com.konsol.core.domain.License;
import com.konsol.core.repository.LicenseRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.service.LicenseService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.LicenseMapper;
import com.konsol.core.service.mapper.SettingsMapper;
import com.konsol.core.web.api.PublicApiDelegate;
import com.konsol.core.web.api.SysApiDelegate;
import com.konsol.core.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.jhipster.web.util.HeaderUtil;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.konsol.core.domain.Bank}.
 */
@Service
public class PublicResource implements PublicApiDelegate {

    private final Logger log = LoggerFactory.getLogger(PublicResource.class);

    private static final String ENTITY_NAME = "bank";
    private final LicenseRepository licenseRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingsMapper settingsMapper;

    private final SettingService settingService;

    private final LicenseService licenseService;

    private final LicenseMapper licenseMapper;

    public PublicResource(
        SettingsMapper settingsMapper,
        SettingService settingService,
        LicenseService licenseService,
        LicenseMapper licenseMapper,
        LicenseRepository licenseRepository
    ) {
        this.settingsMapper = settingsMapper;
        this.settingService = settingService;
        this.licenseService = licenseService;
        this.licenseMapper = licenseMapper;
        this.licenseRepository = licenseRepository;
    }


    @Override
    public ResponseEntity<Boolean> validateLicensePublic() {
        return ResponseEntity.ok(licenseService.validateSystemLicenseKeys());
    }

    @Override
    public ResponseEntity<LicenseDTO> getUserLicense() {
        return ResponseEntity.ok(licenseRepository.findByActiveTrueAndExpiryDateAfter(Instant.now()).stream().findFirst().map(licenseMapper::toApiDto).orElse(null));
    }

    @Override
    public ResponseEntity<LicenseDTO> processLicensePublic(ProcessSuperAdminLicenseRequest request) {
        log.debug("REST request to process super admin license key");

        try {
            if (request.getEncryptedKey() == null || request.getEncryptedKey().trim().isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, "license", "emptyKey", "License key cannot be empty"))
                    .build();
            }

            com.konsol.core.service.dto.LicenseDTO serviceLicense = licenseService.processSuperAdminKey(request.getEncryptedKey());

            return ResponseEntity
                .ok()
                .headers(
                    HeaderUtil.createAlert(
                        applicationName,
                        "License processed successfully",
                        "Valid until: " + serviceLicense.getExpiryDate()
                    )
                )
                .body(licenseMapper.toApiDto(serviceLicense));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, "license", "invalidKey", e.getMessage()))
                .build();
        } catch (Exception e) {
            log.error("Error processing license key", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(
                    HeaderUtil.createFailureAlert(
                        applicationName,
                        true,
                        "license",
                        "processingError",
                        "Error processing license key: " + e.getMessage()
                    )
                )
                .build();
        }
    }
}
