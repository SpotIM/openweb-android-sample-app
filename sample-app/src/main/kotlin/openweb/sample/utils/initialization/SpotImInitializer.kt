package openweb.sample.utils.initialization

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColorInt
import openweb.sample.R
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.screens.settings.clearLoginDelegation
import openweb.sample.ui.screens.settings.customtheme.CombinedThemeColorSetting
import openweb.sample.ui.screens.settings.enums.FontFamilyType
import spotIm.common.api.OWManager
import spotIm.common.api.helpers.OWSpotId
import spotIm.common.api.model.customizations.OWCustomizationContext
import spotIm.common.api.model.customizations.OWCustomizationCustomTextElement
import spotIm.common.api.model.customizations.OWCustomizationElement
import spotIm.common.api.model.customizations.OWCustomizationTextElement
import spotIm.common.api.model.customizations.OWCustomizationViewElement
import spotIm.common.api.model.customizations.OWFontWeight
import spotIm.common.api.model.customizations.OWTheme
import spotIm.common.api.model.logger.OWLogLevel
import spotIm.common.api.model.orientation.OWOrientation
import spotIm.common.api.model.orientation.OWOrientationEnforcement
import spotIm.common.api.ui.customizations.OWCustomizationElements
import spotIm.common.internal.model.customizations.OWElementKey
import spotIm.common.internal.model.customizations.OWThemeMode
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

/**
 * Initializes the OpenWeb SDK with configuration from [SettingsRepository].
 *
 * Applies all sample app settings to the SDK including:
 * - Spot ID and orientation
 * - Theme and dark mode customization
 * - Custom fonts and language settings
 * - Authentication configuration
 * - Comment sorting preferences
 *
 * Subclassed in flavor variants to add environment-specific setup like
 * endpoint overrides and authentication delegation (internal builds only).
 *
 * @param owManager OpenWeb SDK manager instance
 * @param settingsRepository Source of app settings
 */
open class SpotImInitializer(
    private val owManager: OWManager,
    private val settingsRepository: SettingsRepository
) {

    /**
     * Initializes the SDK with all configured settings.
     *
     * @param providedSpotId Optional Spot ID override; uses stored value if null
     */
    fun init(providedSpotId: OWSpotId? = null) {
        val spotId = providedSpotId ?: settingsRepository[SharedPrefsItem.SpotId].orEmpty()
        if (owManager.spotId != spotId) {
            owManager.spotId = spotId
        }

        val enableLandscape = settingsRepository[SharedPrefsItem.EnableLandScape]
        owManager.helpers.orientationEnforcement = when (enableLandscape) {
            true -> OWOrientationEnforcement.EnableAll
            false -> OWOrientationEnforcement.Enable(OWOrientation.PORTRAIT)
        }
        owManager.helpers.loggerConfiguration.apply {
            level = OWLogLevel.Verbose
        }

        val combinedSettings = settingsRepository[SharedPrefsItem.CombinedThemeColors]
        applyThemeSettings(combinedSettings)
        applyCustomFonts()
        applyCustomElementFonts()
        applyCustomElementColors(combinedSettings)
        applyCustomElementCallbacks()
        applyLanguageSettings()
        applyAuthenticationSettings()
        applySortingSettings()

        setupEndpointOverrides(settingsRepository)
        setupAuthentication(settingsRepository)
        setupProfilePath(settingsRepository)
    }

    private fun applyThemeSettings(combinedSettings: List<CombinedThemeColorSetting>?) {
        val isSupportSystemDarkMode = settingsRepository[SharedPrefsItem.SupportSystemDarkMode]
        owManager.ui.customizations.themeEnforcement.apply {
            this.isSupportSystemDarkMode = isSupportSystemDarkMode
            if (!isSupportSystemDarkMode) {
                this.themeMode = if (settingsRepository[SharedPrefsItem.IsDarkMode]) {
                    OWThemeMode.DARK
                } else {
                    OWThemeMode.LIGHT
                }
            }
        }

        // Custom dark theme
        val customDarkColor = settingsRepository[SharedPrefsItem.CustomDarkColor].takeIf { it != -1 }
        owManager.ui.customizations.themeEnforcement.darkColor = customDarkColor ?: "#070707".toColorInt()

        // Custom actions
        owManager.ui.customizations.commentActions.apply {
            commentActionsButtonsColor = settingsRepository[SharedPrefsItem.CommentActionColorStyle]
            commentActionsButtonsFont = settingsRepository[SharedPrefsItem.CommentActionFontStyle]
        }

        // Custom theme colors
        owManager.ui.customizations.customizedTheme = createCustomTheme(combinedSettings)
    }

    private fun applyCustomFonts() {
        owManager.ui.customizations.fontFamily.styleResId =
            if (settingsRepository[SharedPrefsItem.FontFamilyStrategy] == FontFamilyType.Custom) R.style.CustomFontStyle
            else null
    }

    fun resetElementCustomizations() {
        val e = owManager.ui.customizations.elements
        OWCustomizationElements::class.memberProperties.forEach { prop ->
            when (val element = prop.get(e)) {
                is OWCustomizationCustomTextElement -> element.reset()
                is OWCustomizationViewElement<*> -> element.reset()
                is OWCustomizationTextElement -> element.reset()
                is OWCustomizationElement -> element.reset()
            }
        }
    }

    private fun applyCustomElementFonts() {
        val fontSettings = settingsRepository[SharedPrefsItem.CustomFontElements] ?: return
        val elements = owManager.ui.customizations.elements
        for (setting in fontSettings) {
            val family = setting.fontFamily
            val weight = OWFontWeight.fromWeight(setting.fontWeight)
            when (OWElementKey.fromKey(setting.elementKey)) {
                OWElementKey.NAVIGATION_TITLE -> elements.navigationTitle.applyFont(family, weight)
                OWElementKey.COMMENTER_NAME -> elements.commenterName.applyFont(family, weight)
                OWElementKey.COMMENT_BODY -> elements.commentBody.applyFont(family, weight)
                OWElementKey.INPUT_TEXT -> elements.inputText.applyFont(family, weight)
                OWElementKey.AVATAR_TEXT -> elements.avatarText.applyFont(family, weight)
                OWElementKey.COMMENT_ACTIONS -> elements.commentActions.applyFont(family, weight)
                else -> Unit
            }
        }
    }

    private fun applyCustomElementColors(combinedSettings: List<CombinedThemeColorSetting>?) {
        combinedSettings ?: return
        val elements = owManager.ui.customizations.elements
        for (setting in combinedSettings) {
            val elementKey = setting.elementKey ?: continue
            val toggledColor = if (setting.elementToggle) setting.elementColor else null
            when (OWElementKey.fromKey(elementKey)) {
                // Text + view callback elements (preserve any existing font/callback settings)
                OWElementKey.NAVIGATION_TITLE -> elements.navigationTitle.color = toggledColor
                OWElementKey.COMMENTER_NAME -> elements.commenterName.color = toggledColor
                // Text-style only elements
                OWElementKey.COMMENT_ACTIONS -> elements.commentActions.color = toggledColor
                OWElementKey.COMMENT_BODY -> elements.commentBody.color = toggledColor
                OWElementKey.INPUT_TEXT -> elements.inputText.color = toggledColor
                OWElementKey.AVATAR_TEXT -> elements.avatarText.color = toggledColor
                // Color-only elements
                OWElementKey.SUBTITLE -> elements.subtitle.color = toggledColor
                OWElementKey.DETAIL -> elements.detail.color = toggledColor
                OWElementKey.BACKGROUND -> elements.background.color = toggledColor
                OWElementKey.OVERLAY_BACKGROUND -> elements.overlayBackground.color = toggledColor
                OWElementKey.CARD_BACKGROUND -> elements.cardBackground.color = toggledColor
                OWElementKey.BORDER -> elements.border.color = toggledColor
                OWElementKey.SECTION_DIVIDER -> elements.sectionDivider.color = toggledColor
                OWElementKey.CONTENT_DIVIDER -> elements.contentDivider.color = toggledColor
                OWElementKey.DIVIDER -> elements.divider.color = toggledColor
                OWElementKey.SKELETON_GRADIENT_EDGE -> elements.skeletonGradientEdge.color = toggledColor
                OWElementKey.SKELETON_GRADIENT_CENTER -> elements.skeletonGradientCenter.color = toggledColor
                OWElementKey.LOADER -> elements.loader.color = toggledColor
                OWElementKey.BRAND -> elements.brand.color = toggledColor
                OWElementKey.VOTE_UP_SELECTED -> elements.voteUpSelected.color = toggledColor
                OWElementKey.VOTE_DOWN_SELECTED -> elements.voteDownSelected.color = toggledColor
                OWElementKey.VOTE_UP_UNSELECTED -> elements.voteUpUnselected.color = toggledColor
                OWElementKey.VOTE_DOWN_UNSELECTED -> elements.voteDownUnselected.color = toggledColor
                else -> Unit
            }
        }
    }

    private fun applyCustomElementCallbacks() {
        val settings = settingsRepository[SharedPrefsItem.CustomElementToggles] ?: return
        val e = owManager.ui.customizations.elements
        for (setting in settings) {
            val on = setting.isEnabled
            when (OWElementKey.fromKey(setting.elementKey)) {
                OWElementKey.NAVIGATION_TITLE -> e.navigationTitle.applyDemoCallback(on)
                OWElementKey.LOGIN_PROMPT -> e.loginPrompt.applyDemoCallback(on)
                OWElementKey.COMMUNITY_QUESTION -> e.communityQuestion.applyDemoCallback(on)
                OWElementKey.COMMUNITY_GUIDELINES -> e.communityGuidelines.applyDemoCallback(on)
                OWElementKey.SAY_CONTROL_PRE_CONVERSATION -> e.sayControlPreConversation.applyDemoCallback(on)
                OWElementKey.SAY_CONTROL_CONVERSATION -> e.sayControlConversation.applyDemoCallback(on)
                OWElementKey.PRE_CONVERSATION_HEADER_TEXT -> e.preConversationHeaderText.applyDemoCallback(on)
                OWElementKey.PRE_CONVERSATION_HEADER_COUNTER -> e.preConversationHeaderCounter.applyDemoCallback(on)
                OWElementKey.PRE_CONVERSATION_HEADER_USER_COUNT ->
                    e.preConversationHeaderUserCount.applyDemoCallback(on)
                OWElementKey.COMMENTER_NAME -> e.commenterName.applyDemoCallback(on)
                OWElementKey.READ_ONLY_TEXT -> e.readOnlyText.applyDemoCallback(on)
                OWElementKey.EMPTY_STATE_READ_ONLY_TEXT -> e.emptyStateReadOnlyText.applyDemoCallback(on)
                OWElementKey.CONVERSATION_COMMENT_COUNT -> e.conversationCommentCount.applyDemoCallback(on)
                OWElementKey.CONVERSATION_USER_COUNT -> e.conversationUserCount.applyDemoCallback(on)
                OWElementKey.CONVERSATION_SORT_TEXT -> e.conversationSortText.applyDemoCallback(on)
                OWElementKey.CONVERSATION_SORT_DROPDOWN_ITEM -> e.conversationSortDropdownItem.applyDemoCallback(on)
                OWElementKey.NAVIGATION_BACK_ICON -> e.navigationBackIcon.applyDemoCallback(on)
                OWElementKey.NAVIGATION_TOOLBAR -> e.navigationToolbar.applyDemoCallback(on)
                OWElementKey.SHOW_COMMENTS_BUTTON -> e.showCommentsButton.applyDemoCallback(on)
                OWElementKey.COMMENT_CREATION_ACTION_BUTTON -> e.commentCreationActionButton.applyDemoCallback(on)
                OWElementKey.COMMENT_CREATION_ACTION_IMAGE_BUTTON ->
                    e.commentCreationActionImageButton.applyDemoCallback(on)
                OWElementKey.CONVERSATION_FOOTER -> e.conversationFooter.applyDemoCallback(on)
                OWElementKey.CONVERSATION_INFO_LAYOUT -> e.conversationInfoLayout.applyDemoCallback(on)
                OWElementKey.CONVERSATION_SORT_SPINNER -> e.conversationSortSpinner.applyDemoCallback(on)
                else -> Unit
            }
        }
    }

    private fun OWCustomizationTextElement.applyFont(family: String?, weight: OWFontWeight?) {
        fontFamily = family
        fontWeight = weight
    }

    private fun OWCustomizationCustomTextElement.applyFont(family: String?, weight: OWFontWeight?) {
        fontFamily = family
        fontWeight = weight
    }

    /** Demo TextView callback: magenta text in light mode, cyan in dark mode. */
    private fun OWCustomizationCustomTextElement.applyDemoCallback(enabled: Boolean) {
        customizeView = if (enabled) ::demoTextCustomize else null
    }

    /** Demo view callback: semi-transparent magenta background in light, teal in dark. */
    private fun <V : View> OWCustomizationViewElement<V>.applyDemoCallback(enabled: Boolean) {
        customizeView = if (enabled) { view, ctx ->
            view.setBackgroundColor(if (ctx.isDarkModeEnabled) 0x4000BCD4.toInt() else 0x40E91E63.toInt())
        } else null
    }

    private fun demoTextCustomize(view: TextView, ctx: OWCustomizationContext) {
        view.setTextColor(if (ctx.isDarkModeEnabled) Color.CYAN else Color.MAGENTA)
    }

    private fun applyLanguageSettings() {
        owManager.helpers.languageStrategy = settingsRepository[SharedPrefsItem.LanguageStrategy]
        owManager.helpers.localeStrategy = settingsRepository[SharedPrefsItem.LocaleStrategy]
    }

    private fun applyAuthenticationSettings() {
        owManager.authentication.shouldDisplayLoginPromptForGuests = settingsRepository[SharedPrefsItem.ShowLoginPrompt]
    }

    private fun applySortingSettings() {
        owManager.ui.customizations.sorting.initialSortOption = settingsRepository[SharedPrefsItem.InitialSortOption]
    }

    private fun createCustomTheme(combinedList: List<CombinedThemeColorSetting>?): OWTheme {
        val theme = owManager.ui.customizations.customizedTheme

        if (combinedList.isNullOrEmpty()) {
            theme::class.memberProperties
                .filterIsInstance<KMutableProperty1<OWTheme, Any?>>()
                .filter { it.returnType.isMarkedNullable }
                .forEach { it.set(theme, null) }
            return theme
        }

        val properties = theme::class.memberProperties
            .filterIsInstance<KMutableProperty1<OWTheme, Any?>>()
            .associateBy { it.name }

        for (setting in combinedList) {
            val legacyKey = setting.legacyKey ?: continue
            val property = properties[legacyKey] ?: continue
            property.set(theme, if (setting.legacyToggle) setting.legacyColor else null)
        }

        return theme
    }

    /**
     * Hook for flavor-specific endpoint override configuration.
     *
     * Overridden in internal builds to support custom API endpoints for testing.
     * No-op in public builds.
     */
    protected open fun setupEndpointOverrides(settingsRepository: SettingsRepository) = Unit

    /**
     * Hook for flavor-specific authentication delegation setup.
     *
     * Overridden in internal builds to configure SSO and custom login flows.
     * Public builds clear any delegation to use default SDK authentication.
     */
    protected open fun setupAuthentication(settingsRepository: SettingsRepository) = clearLoginDelegation()

    /**
     * Hook for flavor-specific profile URL path configuration.
     *
     * Overridden in internal builds to set custom profile URL paths for testing.
     * No-op in public builds.
     */
    protected open fun setupProfilePath(settingsRepository: SettingsRepository) = Unit
}


