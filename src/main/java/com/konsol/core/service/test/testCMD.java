package com.konsol.core.service.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.config.dbmigrations.InitialSetupMigration;
import com.konsol.core.repository.AuthorityRepository;
import com.konsol.core.service.LicenseService;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.dto.LicenseDTO;
import com.konsol.core.service.dto.LicenseValidationResult;
import java.time.Instant;

import com.konsol.core.service.fingerprint.FingerprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(testCMD.class);

    private final FingerprintService fingerprintService;
    public testCMD(FingerprintService fingerprintService) {

        this.fingerprintService = fingerprintService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Initialize HikVision SDK configuration

            fingerprintService.initializeDevice(
                "192.168.1.100",
                8000,
                "admin",
                "123456"
            );
        }
        catch (Exception e) {
            log.error("Failed to initialize fingerprint device: {}", e.getMessage());
        }

    }
}
