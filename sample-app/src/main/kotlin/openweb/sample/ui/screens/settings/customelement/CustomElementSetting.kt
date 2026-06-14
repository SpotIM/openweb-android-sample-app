package openweb.sample.ui.screens.settings.customelement

import kotlinx.serialization.Serializable

/**
 * Represents the on/off state of a `customizeView` callback for one SDK UI element.
 *
 * When [isEnabled] is true, [SpotImInitializer] assigns a demo lambda (visible magenta
 * tint / color) to the element's `customizeView` so the effect is immediately apparent
 * in the UI.
 *
 * @property elementKey Machine-readable key matching the [OWCustomizationElements] property name.
 * @property displayName Human-readable label shown in the picker list.
 * @property isEnabled Whether the customizeView callback is currently active.
 */
@Serializable
data class CustomElementSetting(
    val elementKey: String,
    val displayName: String,
    val isEnabled: Boolean = false
)
