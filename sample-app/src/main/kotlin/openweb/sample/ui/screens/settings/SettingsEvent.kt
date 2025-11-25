package openweb.sample.ui.screens.settings

import spotIm.common.internal.model.settings.OWEnvironment

/**
 * UI events emitted by [SettingsVM] for the view layer to handle.
 *
 * These events trigger navigation, dialogs, and other UI-specific actions that
 * should be handled by [SettingsFragment] or the host activity.
 */
sealed class SettingsEvent {
    /** Navigate to the custom theme color picker screen */
    data object NavigateToCustomThemePicker : SettingsEvent()

    /** Navigate to the endpoint override configuration screen */
    data object NavigateToEndpointOverride : SettingsEvent()

    /** Navigate to a nested settings submenu */
    data class NavigateToNestedMenu(val screen: SettingsScreen) : SettingsEvent()

    /** Change SDK environment (requires app restart) */
    data class ChangeEnvironment(val environment: OWEnvironment, val baseUrl: String?) : SettingsEvent()

    /** Display a toast message to the user */
    data class ShowToast(val message: String) : SettingsEvent()

    /** Display a confirmation dialog with custom action */
    data class ShowConfirmationDialog(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit
    ) : SettingsEvent()

    /** Display the color picker dialog for dark mode customization */
    data class ShowColorPicker(val currentColor: Int) : SettingsEvent()

    /** Trigger full app restart (used after environment changes) */
    data object TriggerRestart : SettingsEvent()

    /** Reset all settings to defaults and refresh UI */
    data object ResetSettings : SettingsEvent()
}
