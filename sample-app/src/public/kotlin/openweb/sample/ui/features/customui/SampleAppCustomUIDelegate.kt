package openweb.sample.ui.features.customui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.toArgb
import spotIm.common.api.helpers.OWPostId
import spotIm.common.api.model.CustomizableViewType
import spotIm.common.api.ui.customui.CustomUIDelegate
import spotIm.common.internal.helpers.isFalse
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.screens.verticals.theme.CustomUIColors

/**
 * Sample implementation of [CustomUIDelegate] demonstrating SDK view customization.
 *
 * This delegate allows customization of OpenWeb SDK UI elements including colors, text sizes,
 * and styling for navigation bars, comment sections, and user interaction controls.
 * Customizations adapt to light/dark mode based on [isDarkModeEnabled].
 *
 * Enable/disable customization via Settings > Internal Settings > Custom UI Delegation.
 *
 * @param settingsRepository Repository to check if custom UI delegation is enabled
 */
class SampleAppCustomUIDelegate(
    private val settingsRepository: SettingsRepository
) : CustomUIDelegate {
    /**
     * Customizes SDK UI views based on their type and current theme mode.
     *
     * @param viewType Type of view to customize (e.g., text views, buttons, navigation elements)
     * @param view The actual Android View instance to customize
     * @param isDarkModeEnabled Whether dark mode is currently active
     * @param postId Associated post identifier (if applicable)
     */
    override fun customizeView(
        viewType: CustomizableViewType,
        view: View,
        isDarkModeEnabled: Boolean,
        postId: OWPostId
    ) {
        if (settingsRepository[SharedPrefsItem.EnableCustomUIDelegation].isFalse) {
            return
        }
        when (viewType) {
            CustomizableViewType.LOGIN_PROMPT_TEXT_VIEW,
            CustomizableViewType.SAY_CONTROL_IN_PRE_CONVERSATION_TEXT_VIEW,
            CustomizableViewType.SAY_CONTROL_IN_CONVERSATION_TEXT_VIEW -> {
                (view as? TextView)?.apply {
                    setTextColor(
                        if (isDarkModeEnabled) CustomUIColors.Dark.textPrimary.toArgb()
                        else CustomUIColors.Light.textPrimary.toArgb()
                    )
                    textSize = 16f
                }
            }
            
            CustomizableViewType.NAVIGATION_TITLE_TEXT_VIEW -> {
                (view as? TextView)?.apply {
                    setTextColor(
                        if (isDarkModeEnabled) CustomUIColors.Dark.navigationText.toArgb()
                        else CustomUIColors.Light.navigationText.toArgb()
                    )
                    textSize = 18f
                }
            }
            
            CustomizableViewType.NAVIGATION_BACK_IMAGE_VIEW -> {
                (view as? ImageView)?.apply {
                    setColorFilter(
                        if (isDarkModeEnabled) CustomUIColors.Dark.navigationText.toArgb()
                        else CustomUIColors.Light.navigationText.toArgb()
                    )
                }
            }
            
            CustomizableViewType.NAVIGATION_TOOLBAR_VIEW -> {
                view.setBackgroundColor(
                    if (isDarkModeEnabled) CustomUIColors.Dark.navigationBackground.toArgb()
                    else CustomUIColors.Light.navigationBackground.toArgb()
                )
            }
            
            CustomizableViewType.CONVERSATION_USERNAME_TEXT_VIEW -> {
                (view as? TextView)?.apply {
                    setTextColor(
                        if (isDarkModeEnabled) CustomUIColors.Dark.textAccent.toArgb()
                        else CustomUIColors.Light.textAccent.toArgb()
                    )
                }
            }
            
            CustomizableViewType.SHOW_COMMENTS_BUTTON,
            CustomizableViewType.COMMENT_CREATION_ACTION_BUTTON -> {
                view.setBackgroundColor(
                    if (isDarkModeEnabled) CustomUIColors.Dark.buttonBackground.toArgb()
                    else CustomUIColors.Light.buttonBackground.toArgb()
                )
            }
            
            CustomizableViewType.CONVERSATION_FOOTER_VIEW,
            CustomizableViewType.CONVERSATION_INFO_LAYOUT_VIEW -> {
                view.setBackgroundColor(
                    if (isDarkModeEnabled) CustomUIColors.Dark.background.toArgb()
                    else CustomUIColors.Light.background.toArgb()
                )
            }
            
            else -> Unit
        }
    }
}
