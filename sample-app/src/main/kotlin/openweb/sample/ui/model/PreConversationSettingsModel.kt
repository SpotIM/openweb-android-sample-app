package openweb.sample.ui.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import spotIm.common.api.model.settings.conversation.style.OWCommunityGuidelinesStyle
import spotIm.common.api.model.settings.conversation.style.OWCommunityQuestionsStyle
import spotIm.common.api.model.settings.preconversation.style.OWPreConversationStyle.Metrics

/**
 * Configuration model for pre-conversation screen display settings.
 *
 * Controls how the preview of comments (pre-conversation) appears before
 * users open the full conversation screen.
 */
@Keep
@Serializable
data class PreConversationSettingsModel(
    /** Overall pre-conversation style */
    val style: PreConversationStyle,
    /** Number of comment previews to show */
    val numberOfComments: Int = Metrics.DEFAULT_REGULAR_NUMBER_OF_COMMENTS,
    /** Minimum allowed comment previews */
    val minNumberOfComments: Int = Metrics.MIN_NUMBER_OF_COMMENTS,
    /** Maximum allowed comment previews */
    val maxNumberOfComments: Int = Metrics.MAX_NUMBER_OF_COMMENTS,
    /** Style for community guidelines section */
    val communityGuidelinesStyle: PreConversationStyle = PreConversationStyle.Regular,
    /** Style for community questions section */
    val communityQuestionsStyle: PreConversationStyle = PreConversationStyle.Regular
)

/**
 * Display styles for pre-conversation screens.
 *
 * - [None]: Don't show pre-conversation
 * - [Regular]: Standard comment preview with full UI
 * - [Compact]: Condensed comment preview
 * - [ButtonOnly]: Only show "Show Comments" button
 * - [Summary]: Show comment count summary
 * - [Custom]: Custom configuration with fine-grained control
 */
@Keep
@Serializable
enum class PreConversationStyle {
    None, Regular, Compact, ButtonOnly, Summary, Custom
}

fun PreConversationStyle.toOWCommunityGuidelinesStyle(): OWCommunityGuidelinesStyle =
    when (this) {
        PreConversationStyle.Regular -> OWCommunityGuidelinesStyle.Regular
        PreConversationStyle.Compact -> OWCommunityGuidelinesStyle.Compact
        else -> OWCommunityGuidelinesStyle.None
    }

fun PreConversationStyle.toOWCommunityQuestionsStyle(): OWCommunityQuestionsStyle =
    when (this) {
        PreConversationStyle.Regular -> OWCommunityQuestionsStyle.Regular
        PreConversationStyle.Compact -> OWCommunityQuestionsStyle.Compact
        else -> OWCommunityQuestionsStyle.None
    }
