package openweb.sample.data.local

import spotIm.common.api.helpers.OWPostId
import spotIm.common.api.helpers.OWSpotId
import spotIm.common.api.model.OWSortOption
import spotIm.common.api.model.customizations.CommentActionsButtonsColor
import spotIm.common.api.model.customizations.CommentActionsButtonsFont
import spotIm.common.api.model.localization.OWLanguageStrategy
import spotIm.common.api.model.localization.OWLocaleStrategy
import spotIm.common.api.model.localization.OWSupportedLanguage
import spotIm.common.api.model.settings.article.OWReadOnlyMode
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle
import spotIm.common.internal.model.override.EndpointOverride
import spotIm.common.internal.model.settings.OWEnvironment
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.model.ConversationSettingsModel
import openweb.sample.ui.model.PreConversationSettingsModel
import openweb.sample.ui.screens.settings.customtheme.CustomThemeSetting
import openweb.sample.ui.screens.settings.enums.FontFamilyType
import openweb.sample.utils.BuildUtils
import openweb.sample.utils.PreferenceKey

/**
 * A sealed class representing different types of shared preferences items.
 * Each item has a key and a default value.
 *
 * @param T The type of the value stored in the shared preference.
 * @property key The key used to store the value in the shared preferences.
 * @property defaultValue The default value for the shared preference item.
 */
sealed class SharedPrefsItem<T>(val key: String, val defaultValue: T) {

    /**
     * Internal getter for SharedPrefsItem values.
     * * ⚠️ This should NOT be used in business logic!
     * * This is internal ONLY for legacy UI configuration classes
     * (extended from [BaseSettingsItemType]) that define initial UI state. All business logic should
     * use SettingsRepository instead.
     * * @see [SettingsRepository]
     */
    internal fun get(): T = (SharedPreferencesManagerProvider.get().getItem(this) ?: defaultValue)

    data object SpotId : SharedPrefsItem<OWSpotId?>(
        key = PreferenceKey.SpotId.key,
        defaultValue = null
    )

    data object PostId : SharedPrefsItem<OWPostId?>(
        key = PreferenceKey.PostId.key,
        defaultValue = null
    )

    data object EnableLandScape : SharedPrefsItem<Boolean>(
        key = PreferenceKey.EnableLandscape.key,
        defaultValue = false
    )

    data object ShowLogger : SharedPrefsItem<Boolean>(
        key = PreferenceKey.LoggerView.key,
        defaultValue = false
    )

    data object ArticleInformationStrategy :
        SharedPrefsItem<openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy>(
            key = PreferenceKey.ArticleStrategy.key,
            defaultValue = openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy.Server
        )

    data object ArticleSection : SharedPrefsItem<String?>(
        key = PreferenceKey.ArticleSection.key,
        defaultValue = null
    )

    data object ArticleAssociatedUrl : SharedPrefsItem<String?>(
        key = PreferenceKey.ArticleUrl.key,
        defaultValue = null
    )

    data object ArticleHeaderStyle : SharedPrefsItem<Boolean>(
        key = PreferenceKey.HideArticleHeader.key,
        defaultValue = false
    )

    data object EnableSocialReviews : SharedPrefsItem<Boolean>(
        key = PreferenceKey.EnableSocialReviews.key,
        defaultValue = false
    )

    data object IsDarkMode : SharedPrefsItem<Boolean>(
        key = PreferenceKey.IsDarkMode.key,
        defaultValue = true
    )

    data object SupportSystemDarkMode : SharedPrefsItem<Boolean>(
        key = PreferenceKey.SupportSystemDarkMode.key,
        defaultValue = true
    )

    data object ReadOnlyMode : SharedPrefsItem<OWReadOnlyMode>(
        key = PreferenceKey.ReadOnlyMode.key,
        defaultValue = OWReadOnlyMode.SERVER
    )

    data object FontFamilyStrategy : SharedPrefsItem<FontFamilyType>(
        key = PreferenceKey.FontFamilyStrategy.key,
        defaultValue = FontFamilyType.Default
    )

    data object LanguageStrategy : SharedPrefsItem<OWLanguageStrategy>(
        key = PreferenceKey.LanguageStrategy.key,
        defaultValue = OWLanguageStrategy.Device
    )

    data object SupportedLanguage : SharedPrefsItem<OWSupportedLanguage>(
        key = PreferenceKey.LocaleStrategy.key,
        defaultValue = OWSupportedLanguage.English
    )

    data object LocaleStrategy : SharedPrefsItem<OWLocaleStrategy>(
        key = PreferenceKey.LocaleStrategy.key,
        defaultValue = OWLocaleStrategy.Device
    )

    data object CommentActionColorStyle : SharedPrefsItem<CommentActionsButtonsColor>(
        key = PreferenceKey.CommentActionColor.key,
        defaultValue = CommentActionsButtonsColor.DEFAULT
    )

    data object CommentActionFontStyle : SharedPrefsItem<CommentActionsButtonsFont>(
        key = PreferenceKey.CommentActionFont.key,
        defaultValue = CommentActionsButtonsFont.DEFAULT
    )

    data object InitialSortOption : SharedPrefsItem<OWSortOption?>(
        key = PreferenceKey.InitialSortOption.key,
        defaultValue = null
    )

    data object CustomThemeColors : SharedPrefsItem<ArrayList<CustomThemeSetting>?>(
        key = PreferenceKey.CustomThemeColors.key,
        defaultValue = null
    )

    data object CustomDarkColor : SharedPrefsItem<Int?>(
        key = PreferenceKey.CustomDarkColor.key,
        defaultValue = -1
    )

    data object ShowLoginPrompt : SharedPrefsItem<Boolean>(
        key = PreferenceKey.ShowLoginPrompt.key,
        defaultValue = false
    )

    data object PreConversationStyle : SharedPrefsItem<PreConversationSettingsModel>(
        key = PreferenceKey.PreConversationSettings.key,
        defaultValue = PreConversationSettingsModel(
            style = openweb.sample.ui.model.PreConversationStyle.Regular
        )
    )

    data object ConversationStyle : SharedPrefsItem<ConversationSettingsModel>(
        key = PreferenceKey.ConversationSettings.key,
        defaultValue = ConversationSettingsModel(
            style = openweb.sample.ui.model.ConversationStyle.Regular
        )
    )

    data object CommentCreationStyle : SharedPrefsItem<OWCommentCreationStyle>(
        key = PreferenceKey.CommentCreationStyle.key,
        defaultValue = OWCommentCreationStyle.Floating
    )

    data object CommentThreadId : SharedPrefsItem<String?>(
        key = PreferenceKey.CommentThreadId.key,
        defaultValue = null
    )

    data object Environment : SharedPrefsItem<OWEnvironment>(
        key = PreferenceKey.Environment.key,
        defaultValue = OWEnvironment.Production
    )

    data object EnvironmentBaseUrl : SharedPrefsItem<String?>(
        key = PreferenceKey.EnvironmentBaseUrl.key,
        defaultValue = null
    )

    data object MockSSOEnvironment : SharedPrefsItem<OWEnvironment>(
        key = PreferenceKey.MockSsoEnvironment.key,
        defaultValue = OWEnvironment.Production
    )

    data object EndpointOverrides : SharedPrefsItem<List<EndpointOverride>?>(
        key = PreferenceKey.EndpointOverrides.key,
        defaultValue = emptyList()
    )

    data object EnablePullToRefresh : SharedPrefsItem<Boolean>(
        key = PreferenceKey.EnablePullToRefresh.key,
        defaultValue = true
    )

    data object EnableLoginDelegation : SharedPrefsItem<Boolean>(
        key = PreferenceKey.EnableLoginDelegation.key,
        defaultValue = BuildUtils.isInternalBuild()
    )

    data object EnableCustomUIDelegation : SharedPrefsItem<Boolean>(
        key = PreferenceKey.EnableCustomUIDelegation.key,
        defaultValue = false
    )

    data object ProfileUrlPath : SharedPrefsItem<String?>(
        key = PreferenceKey.ProfileUrlPath.key,
        defaultValue = null
    )
}
