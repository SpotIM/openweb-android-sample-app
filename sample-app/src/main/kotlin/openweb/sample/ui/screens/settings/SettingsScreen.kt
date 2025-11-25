package openweb.sample.ui.screens.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Settings screen destinations for hierarchical navigation.
 *
 * Each variant represents a distinct settings page that can be navigated to
 * via [SettingsFragment]. Used with Android Parcelable for fragment arguments.
 */
@Parcelize
enum class SettingsScreen : Parcelable {
    /** Root settings menu with links to all other settings pages */
    MainSettings,
    /** UI customization options (colors, themes, fonts) */
    Customizations,
    /** Authentication and SSO configuration */
    Authentication,
    /** SDK configuration options (language, environment) */
    Configurations,
    /** Article-specific settings (URL, metadata strategy) */
    ArticleSettings,
    /** Pre-conversation and conversation screen style settings */
    ScreensSettings,
    /** Internal debugging and testing tools */
    InternalSettings
}
