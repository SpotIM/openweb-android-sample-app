package openweb.sample.ui.screens.settings.customfont

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Represents the font configuration for one SDK UI element.
 *
 * @property elementKey Machine-readable element identifier (e.g. "navigationTitle").
 * @property displayName Human-readable label shown in the picker list.
 * @property fontFamily Font family name (e.g. "serif", "monospace"). Null = SDK default.
 * @property fontWeight Font weight constant from [android.graphics.Typeface]. Null = SDK default.
 */
@Parcelize
@Serializable
data class CustomFontSetting(
    val elementKey: String,
    val displayName: String,
    val fontFamily: String? = null,
    val fontWeight: Int? = null
) : Parcelable
