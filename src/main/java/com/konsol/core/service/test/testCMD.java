package com.konsol.core.service.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.service.LicenseService;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.dto.LicenseDTO;
import com.konsol.core.service.dto.LicenseValidationResult;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(testCMD.class);
    private final SheftService sheftService;
    private final LicenseService licenseService;
    private final ObjectMapper jacksonObjectMapper;

    public testCMD(SheftService sheftService, LicenseService licenseService, ObjectMapper jacksonObjectMapper) {
        this.sheftService = sheftService;
        this.licenseService = licenseService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // Generate a new license
        // LicenseDTO license = licenseService.generateLicense("clientId", 30);
        // log.info("Generated license: {}", license);

        // Validate the newly generated license
        // LicenseValidationResult result = licenseService.validateLicense(license.getLicenseKey());
        // log.info("Validation result for new license: {}", result);

        // Try to validate the hardcoded license key

        licenseService.generatePublicAndPrivateKeys();

        String data = licenseService.createEncryptedAdminKey("ALI ELAWADY", Instant.now().plus(30, java.time.temporal.ChronoUnit.DAYS));

        // Decrypt the key
        String decryptedJson = licenseService.decryptRSA(data);

        // Parse the JSON into AdminLicenseData
        LicenseService.AdminLicenseData licenseData = jacksonObjectMapper.readValue(decryptedJson, LicenseService.AdminLicenseData.class);

        System.out.println("Decrypted license data: " + licenseData.toString());
        // LicenseDTO licenseDTO=  licenseService.processSuperAdminKey(data);

        // System.out.println( "LicenseDTO: "+licenseDTO.toString());

        //  LicenseValidationResult result = licenseService.validateLicense("qO96f9fIOyUqyqp56Ce_npIMdhEI5VP3uA6L1vJSe1Y");
        //   log.info("Validation result for hardcoded license: {}", result);
    }
}
