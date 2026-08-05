package openweb.sample.data.local

import spotIm.common.api.model.settings.OWAdditionalSettings
import spotIm.common.api.model.settings.commentcreation.OWCommentCreationSettings
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle
import spotIm.common.api.model.settings.commentthread.OWCommentThreadSettings
import spotIm.common.api.model.settings.conversation.OWConversationSettings
import spotIm.common.api.model.settings.conversation.style.OWConversationSpacing
import spotIm.common.api.model.settings.conversation.style.OWConversationStyle
import spotIm.common.api.model.settings.navigation.OWNavigationBackButtonStyle
import spotIm.common.api.model.settings.navigation.OWNavigationToolbarStyle
import spotIm.common.api.model.settings.preconversation.OWPreConversationSettings
import spotIm.common.api.model.settings.preconversation.style.OWPreConversationHeaderStyle
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

    fun providePreConversationSettings() = OWPreConversationSettings(
        preConversationStyle = getPreConversationStyle(),
        preConversationHeaderStyle = getPreConversationHeaderStyle()
    )

    fun provideConversationSettings() = OWConversationSettings(
        conversationStyle = getConversationStyle(),
        allowPullToRefresh = getEnablePullToRefresh(),
        navigationToolbarStyle = getNavigationToolbarStyle(
            styleItem = SharedPrefsItem.ConversationNavToolbarStyle,
            showTitleItem = SharedPrefsItem.NavToolbarShowTitle,
            backButtonItem = SharedPrefsItem.NavBackButtonStyle
        )
    )

    fun provideCommentThreadSettings() = OWCommentThreadSettings(
        allowPullToRefresh = getEnablePullToRefresh(),
        navigationToolbarStyle = getNavigationToolbarStyle(
            styleItem = SharedPrefsItem.CommentThreadNavToolbarStyle,
            showTitleItem = SharedPrefsItem.CommentThreadNavToolbarShowTitle,
            backButtonItem = SharedPrefsItem.CommentThreadNavBackButtonStyle
        )
    )

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

    private fun getNavigationToolbarStyle(
        styleItem: SharedPrefsItem<String>,
        showTitleItem: SharedPrefsItem<Boolean>,
        backButtonItem: SharedPrefsItem<String>
    ): OWNavigationToolbarStyle =
        when (settingsRepository[styleItem]) {
            OWNavigationToolbarStyle.None.toString() -> OWNavigationToolbarStyle.None
            "Custom" -> OWNavigationToolbarStyle.Custom(
                showTitle = settingsRepository[showTitleItem],
                backButtonStyle = getNavBackButtonStyle(backButtonItem)
            )

            else -> OWNavigationToolbarStyle.Regular
        }

    private fun getNavBackButtonStyle(backButtonItem: SharedPrefsItem<String>): OWNavigationBackButtonStyle =
        when (settingsRepository[backButtonItem]) {
            OWNavigationBackButtonStyle.Close.toString() -> OWNavigationBackButtonStyle.Close
            OWNavigationBackButtonStyle.None.toString() -> OWNavigationBackButtonStyle.None
            else -> OWNavigationBackButtonStyle.BackArrow
        }

    private fun getPreConversationHeaderStyle(): OWPreConversationHeaderStyle =
        when (settingsRepository[SharedPrefsItem.PreConversationHeaderStyle]) {
            OWPreConversationHeaderStyle.None.toString() -> OWPreConversationHeaderStyle.None
            "HiddenTitle" -> OWPreConversationHeaderStyle.Custom(showTitle = false)
            else -> OWPreConversationHeaderStyle.Regular
        }
}
