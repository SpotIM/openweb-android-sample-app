package openweb.sample.di

import org.koin.dsl.module
import openweb.sample.data.local.datasource.SettingsLocalDataSource
import openweb.sample.data.repository.SettingsRepository

/**
 * Koin module providing data layer dependencies.
 *
 * Implements the repository pattern with:
 * - [SettingsLocalDataSource]: Local data persistence layer
 * - [SettingsRepository]: Repository exposing settings data to ViewModels
 */
val repositoryModule = module {
    single { SettingsLocalDataSource(sharedPreferencesManager = get()) }
    single { SettingsRepository(settingsLocalDataSource = get()) }
}
