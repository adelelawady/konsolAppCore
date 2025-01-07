package com.konsol.core.service.impl;

import com.konsol.core.domain.Bank;
import com.konsol.core.domain.Settings;
import com.konsol.core.domain.Store;
import com.konsol.core.repository.LicenseRepository;
import com.konsol.core.repository.SettingsRepository;
import com.konsol.core.service.BankService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.StoreService;
import com.konsol.core.service.mapper.LicenseMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;

/**
 * Service Implementation for managing {@link Store}.
 */
@Service
public class SettingsServiceImpl implements SettingService {

    private final Logger log = LoggerFactory.getLogger(SettingService.class);

    private final SettingsRepository settingsRepository;

    private final BankService bankService;

    private final StoreService storeService;

    private final CacheManager cacheManager;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduler);
    private final LicenseRepository licenseRepository;
    private final LicenseMapper licenseMapper;
    private ScheduledFuture<?> scheduledBackupTask;
    private final AtomicReference<String> lastBackupPath = new AtomicReference<>();

    public SettingsServiceImpl(
        SettingsRepository settingsRepository,
        BankService bankService,
        StoreService storeService,
        LicenseRepository licenseRepository,
        LicenseMapper licenseMapper,
        CacheManager cacheManager
    ) {
        this.settingsRepository = settingsRepository;
        this.bankService = bankService;
        this.storeService = storeService;
        this.licenseRepository = licenseRepository;
        this.licenseMapper = licenseMapper;
        this.cacheManager = cacheManager;
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
            //  settings = checkAndAssignSettingsToStore(settings);
            //  settings = checkAndAssignSettingsToBank(settings);
            //  settings = checkAndAssignPlaystationSettingsToStore(settings);
            //  settings = checkAndAssignPlaystationSettingsToBank(settings);
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
    public CompletableFuture<Boolean> performBackup() {
        Settings settings = getSettings();
        if (!settings.isBACKUP_ENABLED()) {
            log.info("Backup is disabled in settings");
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String backupDir = createBackupDirectory(settings, timestamp);

                // Perform MongoDB dump
                boolean success = executeMongoDBDump(settings, backupDir);

                if (success && settings.isBACKUP_INCLUDE_FILES()) {
                    // Backup additional files if configured
                    backupAdditionalFiles(settings, backupDir);
                }

                if (success && settings.isBACKUP_COMPRESS()) {
                    // Compress backup directory
                    compressBackup(backupDir);
                }

                // Clean up old backups
                cleanupOldBackups(settings);

                lastBackupPath.set(backupDir);
                return success;
            } catch (Exception e) {
                log.error("Backup failed", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> performRestore(String backupPath) {
        Settings settings = getSettings();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeMongoDBRestore(settings, backupPath);
            } catch (Exception e) {
                log.error("Restore failed", e);
                return false;
            }
        });
    }

    @Override
    public void scheduleBackups() {
        Settings settings = getSettings();
        if (!settings.isBACKUP_ENABLED()) {
            return;
        }

        cancelScheduledBackups(); // Cancel any existing schedule

        Runnable backupTask = () -> {
            try {
                performBackup().get(30, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("Scheduled backup failed", e);
            }
        };

        switch (settings.getBACKUP_SCHEDULE_TYPE()) {
            case "DAILY":
                scheduleDailyBackup(settings, backupTask);
                break;
            case "WEEKLY":
                scheduleWeeklyBackup(settings, backupTask);
                break;
            case "MONTHLY":
                scheduleMonthlyBackup(settings, backupTask);
                break;
            default:
                log.info("Backup scheduling disabled - manual mode");
        }
    }

    @Override
    public void cancelScheduledBackups() {
        if (scheduledBackupTask != null && !scheduledBackupTask.isCancelled()) {
            scheduledBackupTask.cancel(false);
        }
    }

    @Override
    public String getLatestBackupPath() {
        return lastBackupPath.get();
    }

    private String createBackupDirectory(Settings settings, String timestamp) throws IOException {
        String backupDir = Paths.get(settings.getBACKUP_LOCATION(), "backup_" + timestamp).toString();
        Files.createDirectories(Paths.get(backupDir));
        return backupDir;
    }

    private boolean executeMongoDBDump(Settings settings, String backupDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder(settings.getMONGODB_DUMP_PATH(), "--out=" + backupDir);

            Process process = pb.start();
            return process.waitFor(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("MongoDB dump failed", e);
            return false;
        }
    }

    private boolean executeMongoDBRestore(Settings settings, String backupPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(settings.getMONGODB_RESTORE_PATH(), "--dir=" + backupPath);

            Process process = pb.start();
            return process.waitFor(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("MongoDB restore failed", e);
            return false;
        }
    }

    private void backupAdditionalFiles(Settings settings, String backupDir) {
        // Implement additional file backup logic here
        // For example, copying application files, configurations, etc.
    }

    private void compressBackup(String backupDir) {
        // Implement backup compression logic here
        // For example, creating a ZIP file of the backup directory
    }

    private void cleanupOldBackups(Settings settings) {
        try {
            Path backupLocation = Paths.get(settings.getBACKUP_LOCATION());
            if (!Files.exists(backupLocation)) {
                return;
            }

            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(settings.getBACKUP_RETENTION_DAYS());

            Files
                .list(backupLocation)
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toInstant().isBefore(cutoffDate.toInstant(java.time.ZoneOffset.UTC));
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files
                            .walk(path)
                            .sorted((p1, p2) -> -p1.compareTo(p2))
                            .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (IOException e) {
                                    log.error("Failed to delete old backup file: " + p, e);
                                }
                            });
                    } catch (IOException e) {
                        log.error("Failed to cleanup old backup: " + path, e);
                    }
                });
        } catch (IOException e) {
            log.error("Failed to cleanup old backups", e);
        }
    }

    private void scheduleDailyBackup(Settings settings, Runnable backupTask) {
        LocalTime backupTime = LocalTime.parse(settings.getBACKUP_TIME());
        LocalDateTime nextRun = LocalDateTime.now().withHour(backupTime.getHour()).withMinute(backupTime.getMinute());

        if (nextRun.isBefore(LocalDateTime.now())) {
            nextRun = nextRun.plusDays(1);
        }

        scheduledBackupTask = taskScheduler.schedule(backupTask, nextRun.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    private void scheduleWeeklyBackup(Settings settings, Runnable backupTask) {
        LocalTime backupTime = LocalTime.parse(settings.getBACKUP_TIME());
        List<String> backupDays = settings.getBACKUP_DAYS();

        if (backupDays == null || backupDays.isEmpty()) {
            log.warn("No backup days configured for weekly backup");
            return;
        }

        // Schedule for each configured day
        for (String day : backupDays) {
            try {
                java.time.DayOfWeek dayOfWeek = java.time.DayOfWeek.valueOf(day);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nextRun = now
                    .with(java.time.temporal.TemporalAdjusters.nextOrSame(dayOfWeek))
                    .withHour(backupTime.getHour())
                    .withMinute(backupTime.getMinute());

                if (nextRun.isBefore(now)) {
                    nextRun = nextRun.plusWeeks(1);
                }

                scheduledBackupTask = taskScheduler.schedule(backupTask, nextRun.atZone(java.time.ZoneId.systemDefault()).toInstant());
            } catch (IllegalArgumentException e) {
                log.error("Invalid day configured for weekly backup: " + day, e);
            }
        }
    }

    private void scheduleMonthlyBackup(Settings settings, Runnable backupTask) {
        LocalTime backupTime = LocalTime.parse(settings.getBACKUP_TIME());
        LocalDateTime now = LocalDateTime.now();

        // Schedule for the first day of next month
        LocalDateTime nextRun = now.withDayOfMonth(1).withHour(backupTime.getHour()).withMinute(backupTime.getMinute());

        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusMonths(1);
        }

        scheduledBackupTask = taskScheduler.schedule(backupTask, nextRun.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    private void validateBackupPaths(Settings settings) {
        // Validate backup location
        if (settings.getBACKUP_LOCATION() == null || settings.getBACKUP_LOCATION().trim().isEmpty()) {
            throw new IllegalArgumentException("Backup location cannot be empty");
        }

        Path backupLocation = Paths.get(settings.getBACKUP_LOCATION());
        if (!Files.exists(backupLocation)) {
            try {
                Files.createDirectories(backupLocation);
            } catch (IOException e) {
                throw new IllegalArgumentException("Cannot create backup directory: " + settings.getBACKUP_LOCATION(), e);
            }
        }

        // Validate MongoDB dump path
        if (settings.getMONGODB_DUMP_PATH() == null || settings.getMONGODB_DUMP_PATH().trim().isEmpty()) {
            throw new IllegalArgumentException("MongoDB dump path cannot be empty");
        }

        Path mongoDumpPath = Paths.get(settings.getMONGODB_DUMP_PATH());
        if (!Files.exists(mongoDumpPath)) {
            throw new IllegalArgumentException("MongoDB dump executable not found at: " + settings.getMONGODB_DUMP_PATH());
        }

        // Validate MongoDB restore path
        if (settings.getMONGODB_RESTORE_PATH() == null || settings.getMONGODB_RESTORE_PATH().trim().isEmpty()) {
            throw new IllegalArgumentException("MongoDB restore path cannot be empty");
        }

        Path mongoRestorePath = Paths.get(settings.getMONGODB_RESTORE_PATH());
        if (!Files.exists(mongoRestorePath)) {
            throw new IllegalArgumentException("MongoDB restore executable not found at: " + settings.getMONGODB_RESTORE_PATH());
        }
    }

    private void validateBackupSettings(Settings settings) {
        if (settings.isBACKUP_ENABLED()) {
            // Validate schedule type
            String scheduleType = settings.getBACKUP_SCHEDULE_TYPE();
            if (scheduleType == null || scheduleType.trim().isEmpty()) {
                throw new IllegalArgumentException("Backup schedule type must be specified when backup is enabled");
            }

            // Validate backup time
            String backupTime = settings.getBACKUP_TIME();
            if (backupTime == null || backupTime.trim().isEmpty()) {
                throw new IllegalArgumentException("Backup time must be specified when backup is enabled");
            }
            try {
                LocalTime.parse(backupTime);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid backup time format. Use HH:mm format", e);
            }

            // Validate backup days for weekly schedule
            if ("WEEKLY".equals(scheduleType)) {
                List<String> backupDays = settings.getBACKUP_DAYS();
                if (backupDays == null || backupDays.isEmpty()) {
                    throw new IllegalArgumentException("Backup days must be specified for weekly schedule");
                }
                for (String day : backupDays) {
                    try {
                        java.time.DayOfWeek.valueOf(day);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid backup day: " + day, e);
                    }
                }
            }

            // Validate retention days
            if (settings.getBACKUP_RETENTION_DAYS() <= 0) {
                throw new IllegalArgumentException("Backup retention days must be greater than 0");
            }

            // Validate paths
            validateBackupPaths(settings);
        }
    }

    @Override
    @CacheEvict(value = SettingsRepository.SETTINGS_CACHE, allEntries = true)
    public Settings update(Settings settings) {
        Settings mainSettings = getSettings();
        settings.setId(mainSettings.getId());

        // Validate backup settings before saving
        validateBackupSettings(settings);

        Settings updatedSettings = this.settingsRepository.save(settings);
        // No need to call clearSettingsCache() explicitly since @CacheEvict annotation will handle it

        // Reschedule backups if backup settings have changed
        scheduleBackups();

        return updatedSettings;
    }
}
