package openweb.sample.utils.initialization

import androidx.core.graphics.toColorInt
import openweb.sample.R
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.screens.settings.clearLoginDelegation
import openweb.sample.ui.screens.settings.enums.FontFamilyType
import spotIm.common.api.OWManager
import spotIm.common.api.helpers.OWSpotId
import spotIm.common.api.model.customizations.OWTheme
import spotIm.common.api.model.logger.OWLogLevel
import spotIm.common.api.model.orientation.OWOrientation
import spotIm.common.api.model.orientation.OWOrientationEnforcement
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

        applyThemeSettings()
        applyCustomFonts()
        applyLanguageSettings()
        applyAuthenticationSettings()
        applySortingSettings()

        setupEndpointOverrides(settingsRepository)
        setupAuthentication(settingsRepository)
        setupProfilePath(settingsRepository)
    }

    private fun applyThemeSettings() {
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
        owManager.ui.customizations.customizedTheme = createCustomTheme()
    }

    private fun applyCustomFonts() {
        owManager.ui.customizations.fontFamily.styleResId =
            if (settingsRepository[SharedPrefsItem.FontFamilyStrategy] == FontFamilyType.Custom) R.style.CustomFontStyle
            else null
    }

    private fun applyLanguageSettings() {
        owManager.helpers.languageStrategy = settingsRepository[SharedPrefsItem.LanguageStrategy]
        owManager.helpers.localeStrategy = settingsRepository[SharedPrefsItem.LocaleStrategy]
    }

    private fun applyAuthenticationSettings() {
        owManager.authentication.shouldDisplayLoginPromptForGuests = settingsRepository[SharedPrefsItem.ShowLoginPrompt]
    }

    private fun applySortingSettings() {
        settingsRepository[SharedPrefsItem.InitialSortOption]?.let {
            owManager.ui.customizations.sorting.initialSortOption = it
        }
    }

    private fun createCustomTheme(): OWTheme {
        val theme = owManager.ui.customizations.customizedTheme
        val themeColorsList = settingsRepository[SharedPrefsItem.CustomThemeColors]
        val properties = theme::class.memberProperties.filterIsInstance<KMutableProperty1<OWTheme, Any?>>()

        themeColorsList?.zip(properties)?.forEach { (themeColor, property) ->
            property.set(theme, if (themeColor.toggle) themeColor.color else null)
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
