package openweb.sample.di

import androidx.fragment.app.FragmentActivity
import org.koin.dsl.module
import openweb.sample.ui.navigation.FragmentNavigator
import openweb.sample.ui.navigation.FragmentNavigatorImpl

/**
 * Koin module providing navigation components for the public build variant.
 *
 * The public variant uses [FragmentNavigatorImpl], which provides simplified navigation
 * limited to Settings screens. The main app navigation uses Jetpack Compose Navigation
 * via [VerticalHomeActivity].
 */
val navigationModule = module {
    factory<FragmentNavigator> { (activity: FragmentActivity) ->
        FragmentNavigatorImpl(activity)
    }
}
