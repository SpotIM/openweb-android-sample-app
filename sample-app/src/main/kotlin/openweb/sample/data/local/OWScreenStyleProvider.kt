package openweb.sample.data.local

import spotIm.common.api.model.settings.OWAdditionalSettings
import spotIm.common.api.model.settings.commentcreation.OWCommentCreationSettings
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle
import spotIm.common.api.model.settings.commentthread.OWCommentThreadSettings
import spotIm.common.api.model.settings.conversation.OWConversationSettings
import spotIm.common.api.model.settings.conversation.style.OWConversationSpacing
import spotIm.common.api.model.settings.conversation.style.OWConversationStyle
import spotIm.common.api.model.settings.preconversation.OWPreConversationSettings
import spotIm.common.api.model.settings.preconversation.style.OWPreConversationStyle
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.model.ConversationSettingsModel
import openweb.sample.ui.model.ConversationStyle
import openweb.sample.ui.model.PreConversationSettingsModel
import openweb.sample.ui.model.PreConversationStyle
import openweb.sample.ui.model.toOWCommunityGuidelinesStyle
import openweb.sample.ui.model.toOWCommunityQuestionsStyle

class OWScreenStyleProvider(
    private val settingsRepository: SettingsRepository
) {

    fun provideAdditionalSettings(): OWAdditionalSettings {
        return OWAdditionalSettings(
            preConversationSettings = providePreConversationSettings(),
            conversationSettings = provideConversationSettings(),
            commentThreadSettings = provideCommentThreadSettings(),
            commentCreationSettings = provideCommentCreationSettings()
        )
    }

    fun providePreConversationSettings() = OWPreConversationSettings(preConversationStyle = getPreConversationStyle())

    fun provideConversationSettings() = OWConversationSettings(
        conversationStyle = getConversationStyle(),
        allowPullToRefresh = getEnablePullToRefresh()
    )

    fun provideCommentThreadSettings() = OWCommentThreadSettings(allowPullToRefresh = getEnablePullToRefresh())

    fun provideCommentCreationSettings() = OWCommentCreationSettings(commentCreationStyle = getCommentCreationStyle())

    private fun getCommentCreationStyle(): OWCommentCreationStyle =
        settingsRepository[SharedPrefsItem.CommentCreationStyle]

    private fun getConversationStyle(): OWConversationStyle {
        val conversationSettings = getConversationSettings()
        return when (conversationSettings.style) {
            ConversationStyle.Regular -> OWConversationStyle.Regular
            ConversationStyle.Compact -> OWConversationStyle.Compact
            ConversationStyle.Custom ->
                OWConversationStyle.Custom(
                    communityGuidelinesStyle = conversationSettings.communityGuidelinesStyle
                        .toOWCommunityGuidelinesStyle(),
                    communityQuestionsStyle = conversationSettings.communityQuestionsStyle
                        .toOWCommunityQuestionsStyle(),
                    spacing = when (conversationSettings.conversationSpacingStyle) {
                        ConversationStyle.None,
                        ConversationStyle.Regular -> OWConversationSpacing.Regular

                        ConversationStyle.Compact -> OWConversationSpacing.Compact
                        ConversationStyle.Custom -> {
                            OWConversationSpacing.Custom(
                                conversationSettings.betweenCommentsSpacing,
                                conversationSettings.communityGuidelinesSpacing,
                                conversationSettings.communityGuidelinesSpacing
                            )
                        }
                    }
                )

            else -> OWConversationStyle.Regular
        }
    }

    private fun getPreConversationStyle(): OWPreConversationStyle {
        val preConversationSettings = getPreConversationSettings()
        return when (preConversationSettings.style) {
            PreConversationStyle.Regular -> OWPreConversationStyle.Regular
            PreConversationStyle.Compact -> OWPreConversationStyle.Compact
            PreConversationStyle.ButtonOnly -> OWPreConversationStyle.CtaButtonOnly
            PreConversationStyle.Summary -> OWPreConversationStyle.CtaWithSummary
            PreConversationStyle.Custom ->
                OWPreConversationStyle.Custom(
                    numberOfComments = preConversationSettings.numberOfComments,
                    communityGuidelinesStyle = preConversationSettings.communityGuidelinesStyle
                        .toOWCommunityGuidelinesStyle(),
                    communityQuestionStyle = preConversationSettings.communityQuestionsStyle
                        .toOWCommunityQuestionsStyle()
                )

            else -> OWPreConversationStyle.Regular
        }
    }

    private fun getPreConversationSettings(): PreConversationSettingsModel =
        settingsRepository[SharedPrefsItem.PreConversationStyle]

    private fun getConversationSettings(): ConversationSettingsModel =
        settingsRepository[SharedPrefsItem.ConversationStyle]

    private fun getEnablePullToRefresh(): Boolean = settingsRepository[SharedPrefsItem.EnablePullToRefresh]
}
