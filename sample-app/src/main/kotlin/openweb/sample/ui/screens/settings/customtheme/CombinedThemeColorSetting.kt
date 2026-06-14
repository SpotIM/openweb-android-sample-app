package openweb.sample.ui.screens.settings.customtheme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import spotIm.common.api.model.customizations.UIColor

/**
 * Merged color configuration for one logical "slot" that may have a legacy [OWTheme] property,
 * a new [OWCustomizationElements] field, or both.
 *
 * @property title Human-readable label shown in the combined picker list.
 * @property legacyKey The [OWTheme] property name, or null if this slot has no legacy mapping.
 * @property elementKey The [OWCustomizationElements] field name, or null if this slot has no new-API mapping.
 * @property legacyColor The chosen light/dark color pair for the legacy API. Null until the user picks a color.
 * @property legacyToggle Whether the legacy color override is currently active.
 * @property elementColor The chosen light/dark color pair for the new API. Null until the user picks a color.
 * @property elementToggle Whether the new-API color override is currently active.
 */
@Parcelize
@Serializable
data class CombinedThemeColorSetting(
    val title: String,
    val legacyKey: String? = null,
    val elementKey: String? = null,
    val legacyColor: UIColor? = null,
    val legacyToggle: Boolean = false,
    val elementColor: UIColor? = null,
    val elementToggle: Boolean = false
) : Parcelable
