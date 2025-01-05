package com.konsol.core.service.mapper;

import com.konsol.core.domain.License;
import com.konsol.core.service.api.dto.LicenseDTOHardwareIdentifiers;
import com.konsol.core.service.dto.LicenseDTO;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
public class LicenseMapper {

    /**
     * Convert from service DTO to API DTO
     */
    public com.konsol.core.service.api.dto.LicenseDTO toApiDto(LicenseDTO serviceDto) {
        if (serviceDto == null) {
            return null;
        }

        com.konsol.core.service.api.dto.LicenseDTO apiDto = new com.konsol.core.service.api.dto.LicenseDTO();

        apiDto.setClientId(serviceDto.getClientId());
        apiDto.setLicenseKey(serviceDto.getLicenseKey());

        apiDto.setExpiryDate(OffsetDateTime.parse(serviceDto.getExpiryDate().toString()));

        // Map hardware identifiers
        LicenseDTOHardwareIdentifiers hardwareIds = new LicenseDTOHardwareIdentifiers();
        hardwareIds.setProcessorIdentifier(System.getenv("PROCESSOR_IDENTIFIER"));
        hardwareIds.setBiosSerialNumber(System.getenv("PROCESSOR_ARCHITECTURE"));
        hardwareIds.setDiskDriveSerialNumber(System.getenv("PROCESSOR_ARCHITEW6432"));
        hardwareIds.setMotherboardSerialNumber(System.getenv("NUMBER_OF_PROCESSORS"));

        return apiDto;
    }

    public com.konsol.core.service.api.dto.LicenseDTO toApiDto(License serviceDto) {
        if (serviceDto == null) {
            return null;
        }

        com.konsol.core.service.api.dto.LicenseDTO apiDto = new com.konsol.core.service.api.dto.LicenseDTO();

        apiDto.setClientId(serviceDto.getClientId());
        apiDto.setLicenseKey(serviceDto.getLicenseKey());

        apiDto.setExpiryDate(OffsetDateTime.parse(serviceDto.getExpiryDate().toString()));

        // Map hardware identifiers
        LicenseDTOHardwareIdentifiers hardwareIds = new LicenseDTOHardwareIdentifiers();
        hardwareIds.setProcessorIdentifier(System.getenv("PROCESSOR_IDENTIFIER"));
        hardwareIds.setBiosSerialNumber(System.getenv("PROCESSOR_ARCHITECTURE"));
        hardwareIds.setDiskDriveSerialNumber(System.getenv("PROCESSOR_ARCHITEW6432"));
        hardwareIds.setMotherboardSerialNumber(System.getenv("NUMBER_OF_PROCESSORS"));

        return apiDto;
    }

    /**
     * Convert from API DTO to service DTO
     */
    public LicenseDTO toServiceDto(com.konsol.core.service.api.dto.LicenseDTO apiDto) {
        if (apiDto == null) {
            return null;
        }

        LicenseDTO serviceDto = new LicenseDTO();

        serviceDto.setClientId(apiDto.getClientId());
        serviceDto.setLicenseKey(apiDto.getLicenseKey());
        if (apiDto.getExpiryDate() != null) {
            serviceDto.setExpiryDate(java.time.Instant.parse(apiDto.getExpiryDate().toString()));
        }
        return serviceDto;
    }
}
