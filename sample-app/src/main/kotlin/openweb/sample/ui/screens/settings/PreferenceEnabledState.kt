package openweb.sample.ui.screens.settings

/**
 * UI State for dynamic preference enabled/disabled state.
 * Using enabled/disabled (not visible/hidden) provides better UX -
 * users can see all options and understand why some are unavailable.
 */
data class PreferenceEnabledState(
    // Environment preferences
    val environmentCustomFieldsEnabled: Boolean = false,
    val applyButtonEnabled: Boolean = false,

    // Language preferences
    val customLanguageEnabled: Boolean = false,

    // Article preferences
    val articleAssociatedUrlEnabled: Boolean = false,

    // PreConversation preferences
    val preConversationCustomOptionsEnabled: Boolean = false,

    // Conversation preferences (level 1)
    val conversationCustomOptionsEnabled: Boolean = false,

    // Conversation preferences (level 2 - spacing)
    val conversationSpacingCustomFieldsEnabled: Boolean = false
)
