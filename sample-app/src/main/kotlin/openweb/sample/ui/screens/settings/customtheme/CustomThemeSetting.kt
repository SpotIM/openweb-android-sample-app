package openweb.sample.ui.screens.settings.customtheme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import spotIm.common.api.model.customizations.UIColor

@Parcelize
@Serializable
data class CustomThemeSetting(
    var title: String,
    var toggle: Boolean = false,
    var color: UIColor? = null
) : Parcelable
