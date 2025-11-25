package openweb.sample.ui.screens.settings.customtheme

import com.skydoves.colorpickerview.ColorEnvelope

sealed class CustomThemePickerUIEvent {
    data class OnCustomThemeColorSelected(
        val item: CustomThemeSetting,
        val position: Int,
        val isDark: Boolean
    ) : CustomThemePickerUIEvent()
    data class OnPickerDialogColorSelected(val colorEnvelop: ColorEnvelope) : CustomThemePickerUIEvent()
    data class OnCustomThemeToggleClicked(val position: Int, val toggle: Boolean) : CustomThemePickerUIEvent()
    object OnResetAllClicked : CustomThemePickerUIEvent()
    data class OnRandomColorsClicked(val position: Int) : CustomThemePickerUIEvent()
}
