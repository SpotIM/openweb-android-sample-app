package openweb.sample.di

import androidx.preference.PreferenceDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import openweb.sample.data.local.OWArticleSettingsProvider
import openweb.sample.data.local.OWScreenStyleProvider
import openweb.sample.data.local.SettingsPreferenceDataStore
import openweb.sample.data.local.SharedPreferencesManager
import openweb.sample.utils.ThemeHelper
import spotIm.sdk.OpenWeb

/**
 * Koin module providing core application-level dependencies.
 *
 * Includes:
 * - OpenWeb SDK manager
 * - Shared preferences management
 * - Theme helper for SDK theming
 * - Article and screen style providers
 * - Preference data store for Settings screens
 */
val appModule = module {
    single { OpenWeb.manager }
    single { SharedPreferencesManager(context = androidContext()) }
    single { ThemeHelper(owManager = get()) }

    single { OWArticleSettingsProvider(settingsRepository = get()) }
    single { OWScreenStyleProvider(settingsRepository = get()) }

    single<PreferenceDataStore> { SettingsPreferenceDataStore(settingsRepository = get()) }
}
