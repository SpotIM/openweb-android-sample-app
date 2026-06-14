package openweb.sample.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import openweb.sample.R
import openweb.sample.ui.examples.compose.ComposeFragment
import openweb.sample.ui.examples.conversationbelowvideo.ConversationBelowVideoFragment
import openweb.sample.ui.examples.recycler.PreConversationInRecyclerViewFragment
import openweb.sample.ui.examples.themeoverride.ThemeOverrideTestFragment
import openweb.sample.ui.examples.viewpager.ViewPagerFragment
import openweb.sample.ui.features.conversationcounter.ConversationCounterFragment
import openweb.sample.ui.features.miscellaneous.MiscellaneousFragment
import openweb.sample.ui.features.pushnotifications.PushNotificationsDebugFragment
import openweb.sample.ui.model.OWComponentToDisplay
import openweb.sample.ui.screens.auth.AuthenticationActivity
import openweb.sample.ui.screens.home.InternalHomeFragment
import openweb.sample.ui.screens.home.VerticalHomeFragment
import openweb.sample.ui.screens.mockarticle.MockArticleFragment
import openweb.sample.ui.screens.screensmenu.ExamplesMenuFragment
import openweb.sample.ui.screens.screensmenu.FlowExamplesFragment
import openweb.sample.ui.screens.screensmenu.UIComponentsMenuFragment
import openweb.sample.ui.screens.screensmenu.UIFlowsMenuFragment
import openweb.sample.ui.screens.screensmenu.UIViewsMenuFragment
import openweb.sample.ui.screens.screensmenu.ViewExamplesFragment
import openweb.sample.ui.screens.screensmenu.model.NavigationOption
import openweb.sample.ui.screens.settings.SettingsFragment
import openweb.sample.ui.screens.settings.SettingsScreen
import openweb.sample.ui.screens.settings.overrides.EndpointOverrideFragment
import openweb.sample.ui.screens.settings.elementcustomizations.ElementCustomizationsFragment
import spotIm.common.api.helpers.OWPostId
import spotIm.common.api.model.settings.OWConversationRoute
import spotIm.common.internal.model.settings.OWViewableMode

class FragmentNavigatorImpl(
    private val activity: FragmentActivity
) : FragmentNavigator {

    private val fragmentManager get() = activity.supportFragmentManager

    override fun navigateToHome() {
        // Clear entire back stack and show home
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val homeFragment = InternalHomeFragment.newInstance()
        replaceFragment(homeFragment, addToBackStack = false)
    }

    override fun navigateToUIFlowsMenu() {
        val fragment = UIFlowsMenuFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToUIViewsMenu() {
        val fragment = UIViewsMenuFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToUIComponentsMenu() {
        val fragment = UIComponentsMenuFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToSettings() {
        val fragment = SettingsFragment.newInstance(SettingsScreen.MainSettings)
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToNestedSettingsMenu(screen: SettingsScreen) {
        val fragment = SettingsFragment.newInstance(screen)
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToExamples() {
        val fragment = ExamplesMenuFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToAuthentication() = activity.startActivity(AuthenticationActivity.newInstance(activity))

    override fun navigateToMiscellaneous() {
        val fragment = MiscellaneousFragment()
        replaceFragment(
            fragment,
            addToBackStack = true
        )
    }

    override fun navigateToConversationCounter() {
        val fragment = ConversationCounterFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToElementCustomizations() {
        val fragment = ElementCustomizationsFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToEndpointOverride() {
        val fragment = EndpointOverrideFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToThemeOverrideTest() {
        val fragment = ThemeOverrideTestFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToPushNotificationsDebug() {
        val fragment = PushNotificationsDebugFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToCompose() {
        val fragment = ComposeFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToVerticalDemo() {
        val fragment = VerticalHomeFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToFlowExamples() {
        val fragment = FlowExamplesFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToPreConversationInRecyclerView() {
        val fragment = PreConversationInRecyclerViewFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToViewPager() {
        val fragment = ViewPagerFragment()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToViewExamples() {
        val fragment = ViewExamplesFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToConversationBelowVideo() {
        val fragment = ConversationBelowVideoFragment.newInstance()
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun navigateToMockArticle(
        componentToDisplay: OWComponentToDisplay,
        screenType: OWViewableMode,
        postId: OWPostId,
        conversationRoute: OWConversationRoute?,
        navigationOption: NavigationOption?
    ) {
        val fragment = MockArticleFragment.newInstance(
            componentToDisplay = componentToDisplay,
            screenType = screenType,
            postId = postId,
            conversationRoute = conversationRoute,
            navigationOption = navigationOption
        )
        replaceFragment(fragment, addToBackStack = true)
    }

    override fun popBackStack(): Boolean {
        return if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            true
        } else {
            false
        }
    }

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
