package openweb.sample.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import openweb.sample.ui.features.floatinglogger.FloatingLoggerManager
import openweb.sample.utils.initialization.SpotImInitializer

/**
 * Returns Koin dependency injection modules for the public build variant.
 *
 * The public variant provides simplified, stub implementations of internal-only features
 * to create a clean, production-ready sample app suitable for external distribution.
 *
 * @return List of Koin modules with production-ready component implementations
 */
fun getFlavorModules(): List<Module> {
    return listOf(publicComponentsModule)
}

/**
 * Koin module providing public variant components.
 *
 * Includes stub implementations for:
 * - [FloatingLoggerManager]: No-op logger for public builds (internal debugging only)
 * - [SpotImInitializer]: SDK initialization handler
 */
val publicComponentsModule = module {
    single { FloatingLoggerManager(application = androidContext() as Application, settingsRepository = get()) }
    single<SpotImInitializer> { 
        SpotImInitializer(
            owManager = get(), 
            settingsRepository = get()
        ) 
    }
}
