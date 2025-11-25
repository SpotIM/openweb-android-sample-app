package openweb.sample

import android.app.Application
import android.webkit.WebView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import openweb.sample.di.appModule
import openweb.sample.di.getFlavorModules
import openweb.sample.di.navigationModule
import openweb.sample.di.repositoryModule
import openweb.sample.di.viewModelModule
import openweb.sample.ui.features.floatinglogger.FloatingLoggerManager
import openweb.sample.utils.BuildUtils

/**
 * Application class for the OpenWeb SDK sample app.
 *
 * Initializes:
 * - Koin dependency injection with flavor-specific modules
 * - WebView debugging for internal builds (Appium support)
 * - FloatingLoggerManager for internal builds
 * - Firebase Crashlytics (release builds only)
 */
class SampleApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        val modules = listOf(
            appModule,
            navigationModule,
            repositoryModule,
            viewModelModule
        ) + getFlavorModules()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@SampleApplication)
            modules(modules)
        }

        if (BuildUtils.isInternalBuild()) {
            WebView.setWebContentsDebuggingEnabled(true)

            val floatingLoggerManager: FloatingLoggerManager by inject()
            floatingLoggerManager
        }

        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
    }
}
