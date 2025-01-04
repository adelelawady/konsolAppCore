package com.konsol.core.service.impl;

import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Settings;
import com.konsol.core.domain.Store;
import com.konsol.core.repository.SettingsRepository;
import com.konsol.core.service.BankService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.StoreService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Store}.
 */
@Service
public class SettingsServiceImpl implements SettingService {

    private final Logger log = LoggerFactory.getLogger(SettingService.class);

    private final SettingsRepository settingsRepository;

    private final BankService bankService;

    private final StoreService storeService;

    public SettingsServiceImpl(SettingsRepository settingsRepository, BankService bankService, StoreService storeService) {
        this.settingsRepository = settingsRepository;
        this.bankService = bankService;
        this.storeService = storeService;
    }

    @Override
    public Settings getSettings() {
        Settings settings = settingsRepository.findFirstByOrderById();
        if (settings == null) {
            Settings settingsToSave = new Settings();
            settingsToSave = this.settingsRepository.save(settingsToSave);
            settingsToSave = checkAndAssignSettingsToStore(settingsToSave);
            settingsToSave = checkAndAssignSettingsToBank(settingsToSave);
            settingsToSave = checkAndAssignPlaystationSettingsToStore(settingsToSave);
            settingsToSave = checkAndAssignPlaystationSettingsToBank(settingsToSave);
            return settingsToSave;
        } else {
            settings = checkAndAssignSettingsToStore(settings);
            settings = checkAndAssignSettingsToBank(settings);
            settings = checkAndAssignPlaystationSettingsToStore(settings);
            settings = checkAndAssignPlaystationSettingsToBank(settings);
            return settings;
        }
    }

    private Settings checkAndAssignSettingsToStore(Settings settings) {
        String mainSelectedStoreId = settings.getMAIN_SELECTED_STORE_ID();
        if (mainSelectedStoreId == null || mainSelectedStoreId.isEmpty()) {
            mainSelectedStoreId = storeService.findFirstByOrderById().map(Store::getId).orElseThrow();
        } else if (storeService.findOneDomain(mainSelectedStoreId).isEmpty()) {
            mainSelectedStoreId = storeService.findFirstByOrderById().map(Store::getId).orElseThrow();
        }
        settings.setMAIN_SELECTED_STORE_ID(mainSelectedStoreId);
        return settingsRepository.save(settings);
    }

    private Settings checkAndAssignSettingsToBank(Settings settings) {
        String mainSelectedBankId = settings.getMAIN_SELECTED_BANK_ID();
        if (mainSelectedBankId == null || mainSelectedBankId.isEmpty()) {
            mainSelectedBankId = bankService.findFirstByOrderById().map(Bank::getId).orElseThrow();
        } else if (bankService.findOneDomain(mainSelectedBankId).isEmpty()) {
            mainSelectedBankId = bankService.findFirstByOrderById().map(Bank::getId).orElseThrow();
        }
        settings.setMAIN_SELECTED_BANK_ID(mainSelectedBankId);
        return settingsRepository.save(settings);
    }

    private Settings checkAndAssignPlaystationSettingsToStore(Settings settings) {
        String playstationSelectedStoreId = settings.getPLAYSTATION_SELECTED_STORE_ID();
        if (playstationSelectedStoreId == null || playstationSelectedStoreId.isEmpty()) {
            playstationSelectedStoreId = storeService.findFirstByOrderById().map(Store::getId).orElseThrow();
        } else if (storeService.findOneDomain(playstationSelectedStoreId).isEmpty()) {
            playstationSelectedStoreId = storeService.findFirstByOrderById().map(Store::getId).orElseThrow();
        }
        settings.setPLAYSTATION_SELECTED_STORE_ID(playstationSelectedStoreId);
        return settingsRepository.save(settings);
    }

    private Settings checkAndAssignPlaystationSettingsToBank(Settings settings) {
        String playstationSelectedBankId = settings.getPLAYSTATION_SELECTED_BANK_ID();
        if (playstationSelectedBankId == null || playstationSelectedBankId.isEmpty()) {
            playstationSelectedBankId = bankService.findFirstByOrderById().map(Bank::getId).orElseThrow();
        } else if (bankService.findOneDomain(playstationSelectedBankId).isEmpty()) {
            playstationSelectedBankId = bankService.findFirstByOrderById().map(Bank::getId).orElseThrow();
        }
        settings.setPLAYSTATION_SELECTED_BANK_ID(playstationSelectedBankId);
        return settingsRepository.save(settings);
    }

    @Override
    public Settings update(Settings settings) {
        Settings mainSettings = getSettings();
        settings.setId(mainSettings.getId());
        return this.settingsRepository.save(settings);
    }
}
