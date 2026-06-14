package openweb.sample.ui.screens.settings.elementcustomizations

import spotIm.common.api.model.customizations.OWFontWeight

data class ElementCustomizationSetting(
    val elementKey: String,
    val displayName: String,
    val callbackEnabled: Boolean?,
    val hasFontControl: Boolean,
    val fontFamily: String? = null,
    val fontWeight: OWFontWeight? = null,
    val colorNewEnabled: Boolean? = null,
    val colorNewLight: Int? = null,
    val colorNewDark: Int? = null,
    val colorLegacyEnabled: Boolean? = null,
    val colorLegacyLight: Int? = null,
    val colorLegacyDark: Int? = null,
    val legacyColorKey: String? = null,
    val modernColorKey: String? = null,
    val colorIndex: Int? = null,
    val customFontInput: String? = null,
) {
    val hasCallback: Boolean get() = callbackEnabled != null
    val hasColorSetting: Boolean get() = colorNewEnabled != null || colorLegacyEnabled != null
    val isModified: Boolean get() =
        callbackEnabled == true || fontFamily != null || fontWeight != null ||
            colorNewEnabled == true || colorLegacyEnabled == true
}
