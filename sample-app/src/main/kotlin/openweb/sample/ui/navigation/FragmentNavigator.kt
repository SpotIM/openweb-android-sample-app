package openweb.sample.ui.navigation

import openweb.sample.ui.model.OWComponentToDisplay
import openweb.sample.ui.screens.screensmenu.model.NavigationOption
import openweb.sample.ui.screens.settings.SettingsScreen
import spotIm.common.api.helpers.OWPostId
import spotIm.common.api.model.settings.OWConversationRoute
import spotIm.common.internal.model.settings.OWViewableMode

/**
 * Navigation interface for fragment-based navigation in the sample app.
 * Provides centralized navigation logic with consistent animations and backstack management.
 */
interface FragmentNavigator {

    /**
     * Navigate to the home screen (root fragment).
     * Clears the entire back stack.
     */
    fun navigateToHome() = Unit

    /**
     * Navigate to the UI Flows menu screen.
     */
    fun navigateToUIFlowsMenu() = Unit

    /**
     * Navigate to the UI Views menu screen.
     */
    fun navigateToUIViewsMenu() = Unit

    /**
     * Navigate to the UI Components menu screen.
     */
    fun navigateToUIComponentsMenu() = Unit

    /**
     * Navigate to the settings screen.
     */
    fun navigateToSettings() = Unit

    /**
     * Navigate to a nested settings menu.
     * @param screen The settings screen to navigate to
     */
    fun navigateToNestedSettingsMenu(screen: SettingsScreen) = Unit

    /**
     * Navigate to the examples menu screen.
     */
    fun navigateToExamples() = Unit

    /**
     * Navigate to the authentication screen.
     */
    fun navigateToAuthentication() = Unit

    /**
     * Navigate to the miscellaneous utilities screen.
     */
    fun navigateToMiscellaneous() = Unit

    /**
     * Navigate to the conversation counter screen.
     */
    fun navigateToConversationCounter() = Unit

    /**
     * Navigate to the custom theme picker screen.
     */
    fun navigateToCustomThemePicker() = Unit

    /**
     * Navigate to the endpoint override settings screen.
     */
    fun navigateToEndpointOverride() = Unit

    /**
     * Navigate to the theme override test screen.
     */
    fun navigateToThemeOverrideTest() = Unit

    /**
     * Navigate to the flow examples screen.
     */
    fun navigateToFlowExamples() = Unit

    /**
     * Navigate to the pre-conversation in recycler view screen.
     */
    fun navigateToPreConversationInRecyclerView() = Unit

    /**
     * Navigate to the view pager screen.
     */
    fun navigateToViewPager() = Unit

    /**
     * Navigate to the view examples screen.
     */
    fun navigateToViewExamples() = Unit

    /**
     * Navigate to the conversation below video screen.
     */
    fun navigateToConversationBelowVideo() = Unit

    /**
     * Navigate to the compose examples screen.
     */
    fun navigateToCompose() = Unit

    /**
     * Navigate to the vertical demo screen (Compose).
     */
    fun navigateToVerticalDemo() = Unit

    /**
     * Navigate to the mock article screen.
     */
    fun navigateToMockArticle(
        componentToDisplay: OWComponentToDisplay,
        screenType: OWViewableMode,
        postId: OWPostId,
        conversationRoute: OWConversationRoute? = null,
        navigationOption: NavigationOption? = null
    ) = Unit

    /**
     * Pop the back stack (go back one screen).
     * @return true if a fragment was popped, false if the back stack is empty
     */
    fun popBackStack(): Boolean
}
