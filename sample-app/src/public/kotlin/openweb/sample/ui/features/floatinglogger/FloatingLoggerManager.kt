package openweb.sample.ui.features.floatinglogger

import android.app.Application
import openweb.sample.data.repository.SettingsRepository

/**
 * No-op stub implementation of FloatingLoggerManager for public builds.
 *
 * The floating logger is an internal debugging tool not included in public distributions.
 * This stub ensures the public build compiles and runs without the internal logging UI.
 *
 * @param application Application context (unused in public variant)
 * @param settingsRepository Settings repository (unused in public variant)
 */
class FloatingLoggerManager(
    application: Application,
    settingsRepository: SettingsRepository
) {
    /**
     * No-op in public builds. Logger is internal-only.
     */
    fun enableFloatingLogger() = Unit

    /**
     * No-op in public builds. Logger is internal-only.
     */
    fun disableFloatingLogger() = Unit

    /**
     * No-op in public builds. Logger is internal-only.
     *
     * @param message Log message (ignored)
     * @param logType Optional log type/level (ignored)
     */
    fun addLog(message: String, logType: Any? = null) = Unit
}
