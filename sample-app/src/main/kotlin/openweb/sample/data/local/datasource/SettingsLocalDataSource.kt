package openweb.sample.data.local.datasource

import openweb.sample.data.local.SharedPreferencesManager
import openweb.sample.data.local.SharedPrefsItem

/**
 * Local data source for settings operations.
 * Provides access to all app settings stored in SharedPreferences.
 * This is the single source of truth for settings data.
 * * Uses generic get/set methods with SharedPrefsItem to maintain type safety
 * while avoiding boilerplate for each individual setting.
 */
class SettingsLocalDataSource(
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    /**
     * Operator overload to get a setting value using bracket notation.
     */
    operator fun <T> get(item: SharedPrefsItem<T>): T = sharedPreferencesManager.getItem(item) ?: item.defaultValue

    /**
     * Operator overload to set a setting value using bracket notation.
     */
    operator fun <T> set(item: SharedPrefsItem<T>, value: T) = sharedPreferencesManager.setItem(item, value)

    /**
     * Clears all settings from SharedPreferences, resetting to defaults,
     * while preserving specified keys.
     *
     * @param keysToPreserve List of preference keys that should not be cleared
     * @return true if environment key was set (and thus app restart is needed)
     */
    fun clearAllSettings(keysToPreserve: List<String>): Boolean =
        sharedPreferencesManager.clearAllSettings(keysToPreserve)
}
