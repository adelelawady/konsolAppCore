package com.konsol.core.web.rest.api;

import com.konsol.core.service.api.dto.*;
import com.konsol.core.web.api.SysApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * REST controller for managing {@link com.konsol.core.domain.Pk}.
 */
@Service
public class SystemResource implements SysApiDelegate {

    //    private final SystemConfigurationRepository systemConfigurationRepository;

    //  private final SystemConfigurationMapper systemConfigurationMapper;

    public SystemResource(
        // SystemConfigurationRepository systemConfigurationRepository,
        // SystemConfigurationMapper systemConfigurationMapper
    ) {
        // this.systemConfigurationRepository = systemConfigurationRepository;
        // this.systemConfigurationMapper = systemConfigurationMapper;
    }

    @Override
    public ResponseEntity<SysOptions> updateGlobalSystemOptions(SysOptions sysOptions) {
        //  SystemConfiguration systemConfiguration = getSystemConfigurations();
        //  systemConfiguration.setSysOptions(sysOptions);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<SysOptions> getGlobalSystemOptions() {
        return ResponseEntity.ok(null);
    }
    /*
    public SystemConfiguration getSystemConfigurations() {
        // List<SystemConfiguration> systemConfigurations = systemConfigurationRepository.findAll();
        // if (systemConfigurations.isEmpty()) {
        SystemConfiguration systemConfigurationsUpdate = new SystemConfiguration();
        SysOptions sysOptions = new SysOptions();
        sysOptions.setContactInfo(new ContactInfo());
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setSalesInvoiceOptions(new GlobalSettingsSalesInvoiceOptions());
        globalSettings.setPurchaseInvoiceOptions(new GlobalSettingsPurchaseInvoiceOptions());
        globalSettings.setItemsOptions(new GlobalSettingsItemsOptions());
        sysOptions.setSettings(globalSettings);
        systemConfigurationsUpdate.setSysOptions(sysOptions);
        return null;
        // } else {
        //     Optional<SystemConfiguration> systemConfigurationOptional = systemConfigurations.stream().findFirst();
        //     return systemConfigurationOptional.orElseGet(null);
        // }
    }
    */

}
