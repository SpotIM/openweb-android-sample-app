package openweb.sample.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import openweb.sample.ui.mainactivity.MainActivityVM
import openweb.sample.ui.screens.settings.SettingsVM
import openweb.sample.ui.screens.settings.customtheme.CustomThemePickerVM
import openweb.sample.ui.screens.verticals.screens.ArticleVerticalScreenVM
import openweb.sample.ui.screens.verticals.screens.SiderailVerticalScreenVM
import openweb.sample.ui.screens.verticals.screens.SportVerticalScreenVM
import openweb.sample.ui.screens.verticals.screens.VideoVerticalScreenVM

/**
 * Koin module providing ViewModel dependencies shared across build variants.
 *
 * Includes ViewModels for:
 * - Main activity navigation and initialization
 * - Settings screens and customization
 * - Content vertical screens (News, Sports, Finance, Video, etc.)
 *
 * Note: Flavor-specific ViewModels are provided in [getFlavorModules].
 */
val viewModelModule = module {
    viewModel { MainActivityVM(owManager = get(), settingsRepository = get()) }

    viewModel { SettingsVM(floatingLoggerManager = get(), settingsRepository = get()) }
    viewModel { CustomThemePickerVM(settingsRepository = get()) }

    viewModel { params ->
        ArticleVerticalScreenVM(
            mockData = params.get(),
            screenStyleProvider = get(),
            spotImInitializer = get()
        )
    }
    viewModel { params ->
        VideoVerticalScreenVM(
            mockData = params.get(),
            screenStyleProvider = get(),
            spotImInitializer = get(),
            owManager = get()
        )
    }
    viewModel { params ->
        SiderailVerticalScreenVM(
            mockData = params.get(),
            screenStyleProvider = get(),
            spotImInitializer = get()
        )
    }
    viewModel { params ->
        SportVerticalScreenVM(
            mockData = params.get(),
            screenStyleProvider = get(),
            spotImInitializer = get()
        )
    }
}
