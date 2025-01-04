package com.konsol.core.service;

import com.konsol.core.domain.Settings;
import java.util.concurrent.CompletableFuture;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Store}.
 */
public interface SettingService {
    Settings getSettings();

    Settings update(Settings settings);

    /**
     * Backup related methods
     */
    CompletableFuture<Boolean> performBackup();
    CompletableFuture<Boolean> performRestore(String backupPath);
    void scheduleBackups();
    void cancelScheduledBackups();
    String getLatestBackupPath();
    /*

// Perform manual backup
settingService.performBackup()
    .thenAccept(success -> {
        if (success) {
            log.info("Backup completed successfully");
        } else {
            log.error("Backup failed");
        }
    });

// Restore from backup
settingService.performRestore("/path/to/backup")
    .thenAccept(success -> {
        if (success) {
            log.info("Restore completed successfully");
        } else {
            log.error("Restore failed");
        }
    });

// Start scheduled backups
settingService.scheduleBackups();

// Cancel scheduled backups
settingService.cancelScheduledBackups();

        */
}
