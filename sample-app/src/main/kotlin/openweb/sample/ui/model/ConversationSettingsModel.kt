package openweb.sample.ui.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import spotIm.common.api.model.settings.conversation.style.OWCommunityGuidelinesStyle
import spotIm.common.api.model.settings.conversation.style.OWCommunityQuestionsStyle
import spotIm.common.api.model.settings.conversation.style.OWConversationSpacing

/**
 * Configuration model for full conversation screen display settings.
 *
 * Controls the layout, spacing, and style of the full conversation view
 * including comments, guidelines, and questions sections.
 */
@Keep
@Serializable
data class ConversationSettingsModel(
    /** Overall conversation style */
    val style: ConversationStyle,
    /** Style for community guidelines section */
    val communityGuidelinesStyle: ConversationStyle = ConversationStyle.Regular,
    /** Style for community questions section */
    val communityQuestionsStyle: ConversationStyle = ConversationStyle.Regular,
    /** Spacing configuration style */
    val conversationSpacingStyle: ConversationStyle = ConversationStyle.Regular,
    /** Vertical spacing between comments (dp) */
    val betweenCommentsSpacing: Int = OWConversationSpacing.Metrics.DEFAULT_SPACE_BETWEEN_COMMENTS,
    /** Vertical spacing for guidelines section (dp) */
    val communityGuidelinesSpacing: Int = OWConversationSpacing.Metrics.DEFAULT_SPACE_COMMUNITY_GUIDELINES,
    /** Vertical spacing for questions section (dp) */
    val communityQuestionsSpacing: Int = OWConversationSpacing.Metrics.DEFAULT_SPACE_COMMUNITY_QUESTIONS,
    /** Minimum allowed spacing value (dp) */
    val minSpace: Int = OWConversationSpacing.Metrics.MIN_SPACE,
    /** Maximum allowed spacing value (dp) */
    val maxSpace: Int = OWConversationSpacing.Metrics.MAX_SPACE
)

/**
 * Display styles for conversation screens.
 *
 * - [None]: Don't show this section
 * - [Regular]: Standard display with default spacing
 * - [Compact]: Condensed display with reduced spacing
 * - [Custom]: Custom configuration with fine-grained spacing control
 */
@Keep
@Serializable
enum class ConversationStyle {
    None, Regular, Compact, Custom
}

/**
 * Types of spacing that can be customized in conversation screens.
 */
@Keep
enum class ConversationSpacingType {
    /** Spacing between individual comments */
    BetweenComments,
    /** Spacing around community guidelines section */
    Guidelines,
    /** Spacing around community questions section */
    Question
}

fun ConversationStyle.toOWCommunityGuidelinesStyle(): OWCommunityGuidelinesStyle =
    when (this) {
        ConversationStyle.Regular -> OWCommunityGuidelinesStyle.Regular
        ConversationStyle.Compact -> OWCommunityGuidelinesStyle.Compact
        else -> OWCommunityGuidelinesStyle.None
    }

fun ConversationStyle.toOWCommunityQuestionsStyle(): OWCommunityQuestionsStyle =
    when (this) {
        ConversationStyle.Regular -> OWCommunityQuestionsStyle.Regular
        ConversationStyle.Compact -> OWCommunityQuestionsStyle.Compact
        else -> OWCommunityQuestionsStyle.None
    }
