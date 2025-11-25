package openweb.sample.data.local

import org.koin.java.KoinJavaComponent.inject

/**
 * Provider object for accessing SharedPreferencesManager via Koin.
 * This avoids using lateinit and provides thread-safe lazy initialization.
 */
object SharedPreferencesManagerProvider {
    private val manager: SharedPreferencesManager by inject(SharedPreferencesManager::class.java)

    fun get(): SharedPreferencesManager = manager
}
