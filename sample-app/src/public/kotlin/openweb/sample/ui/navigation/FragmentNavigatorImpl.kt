package openweb.sample.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import openweb.sample.R
import openweb.sample.ui.screens.settings.SettingsFragment
import openweb.sample.ui.screens.settings.SettingsScreen
import openweb.sample.ui.screens.settings.customtheme.CustomThemePickerFragment

/**
 * Public variant implementation of [FragmentNavigator] providing Settings screen navigation.
 *
 * This simplified navigator is used in the public sample app, which uses [VerticalHomeActivity]
 * (Jetpack Compose) as its main entry point and only needs fragment-based navigation for
 * Settings screens.
 *
 * @param activity The host FragmentActivity for fragment transactions
 */
class FragmentNavigatorImpl(
    private val activity: FragmentActivity
) : FragmentNavigator {

    private val fragmentManager get() = activity.supportFragmentManager

    /**
     * Navigates to the main Settings screen, clearing any existing back stack.
     */
    override fun navigateToSettings() {
        val fragment = SettingsFragment.newInstance(SettingsScreen.MainSettings)
        replaceFragment(fragment, addToBackStack = false)
    }

    /**
     * Navigates to a nested Settings submenu.
     *
     * @param screen The specific settings screen to display
     */
    override fun navigateToNestedSettingsMenu(screen: SettingsScreen) {
        val fragment = SettingsFragment.newInstance(screen)
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToCustomThemePicker() {
        val fragment = CustomThemePickerFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    /**
     * Attempts to pop the fragment back stack.
     *
     * @return true if a fragment was popped, false if the back stack was empty
     */
    override fun popBackStack(): Boolean =
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            true
        } else {
            false
        }

    /**
     * Replaces the current fragment with slide animations.
     *
     * @param fragment Fragment to display
     * @param addToBackStack Whether to add this transaction to the back stack
     */
    internal fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean
    ) {
        val tag = fragment::class.java.simpleName
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, fragment, tag)
            .apply { if (addToBackStack) addToBackStack(tag) }
            .commit()
    }
}
