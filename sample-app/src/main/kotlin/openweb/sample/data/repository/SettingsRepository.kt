package openweb.sample.data.repository

import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.local.datasource.SettingsLocalDataSource

/**
 * Repository providing type-safe access to app settings via SharedPreferences.
 *
 * Serves as the single source of truth for all app configuration, abstracting
 * the data source layer from ViewModels and business logic.
 *
 * Uses operator overloading with [SharedPrefsItem] for type-safe, concise access:
 * ```
 * // Reading
 * val spotId: String = repository[SharedPrefsItem.SpotId]
 *
 * // Writing
 * repository[SharedPrefsItem.SpotId] = "sp_123"
 * ```
 *
 * @param settingsLocalDataSource Data source handling SharedPreferences persistence
 */
class SettingsRepository(
    private val settingsLocalDataSource: SettingsLocalDataSource
) {

    /**
     * Reads a setting value with compile-time type safety.
     *
     * @param item Typed setting key defining the preference type
     * @return Current value of the setting
     */
    operator fun <T> get(item: SharedPrefsItem<T>): T = settingsLocalDataSource[item]

    /**
     * Writes a setting value with compile-time type safety.
     *
     * @param item Typed setting key defining the preference type
     * @param value New value to persist
     */
    operator fun <T> set(item: SharedPrefsItem<T>, value: T) {
        settingsLocalDataSource[item] = value
    }

    /**
     * Clears all settings from SharedPreferences, resetting to defaults,
     * while preserving specified keys.
     *
     * @param keysToPreserve List of preference keys that should not be cleared
     * @return true if environment key was set (and thus app restart is needed)
     */
    fun clearAllSettings(keysToPreserve: List<String>): Boolean =
        settingsLocalDataSource.clearAllSettings(keysToPreserve)
}
