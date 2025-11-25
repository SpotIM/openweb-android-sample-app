package openweb.sample.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import spotIm.common.api.OWManager
import spotIm.common.internal.helpers.isFalse

class ThemeHelper(private val owManager: OWManager) {

    fun getBottomSheetBackgroundColor(context: Context, isBottomSheetVisible: Boolean): Color {
        if (isBottomSheetVisible.isFalse) return Color.Transparent

        val customizations = owManager.ui.customizations
        return if (customizations.themeEnforcement.isDarkModeEnabled(context)) {
            Color(customizations.themeEnforcement.darkColor)
        } else {
            Color.White
        }
    }
}
