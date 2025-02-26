package com.konsol.core.service.test;

import com.konsol.core.service.LicenseService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(testCMD.class);

    private final LicenseService licenseService;

    public testCMD(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Override
    public void run(String... args) throws Exception {
        String licenceKey = licenseService.createEncryptedAdminKey("admin", Instant.now().plusSeconds(60L * 60 * 24 * 365 * 5000));
        log.info("licenceKey: " + licenceKey);
    }
}
