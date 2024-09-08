package com.konsol.core.service;

import com.konsol.core.domain.Settings;

/**
 * Service Interface for managing {@link com.konsol.core.domain.Store}.
 */
public interface SettingService {
    Settings getSettings();

    Settings update(Settings settings);
}
