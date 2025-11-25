package openweb.sample.data.local

import androidx.preference.PreferenceDataStore
import spotIm.common.api.model.OWSortOption
import spotIm.common.api.model.customizations.CommentActionsButtonsColor
import spotIm.common.api.model.customizations.CommentActionsButtonsFont
import spotIm.common.api.model.localization.OWLanguageStrategy
import spotIm.common.api.model.localization.OWLocaleStrategy
import spotIm.common.api.model.localization.OWSupportedLanguage
import spotIm.common.api.model.settings.article.OWReadOnlyMode
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle
import spotIm.common.internal.model.customizations.OWThemeMode
import spotIm.common.internal.model.settings.OWEnvironment
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.model.ConversationStyle
import openweb.sample.ui.model.PreConversationStyle
import openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy
import openweb.sample.ui.screens.settings.enums.FontFamilyType
import openweb.sample.utils.PreferenceKey

/**
 * Custom PreferenceDataStore that connects androidx.preference with SettingsRepository.
 * Lives in the data layer as it directly interacts with SettingsRepository.
 * UI layer consumes this via the abstract PreferenceDataStore interface.
 *
 * Note: It's acceptable for this infrastructure class to reference PreferenceKey from
 * the UI layer since they're just constant definitions, not business logic.
 */
class SettingsPreferenceDataStore(
    private val settingsRepository: SettingsRepository
) : PreferenceDataStore() {

    // Cache to remember the last custom language selection across strategy switches
    private var lastCustomLanguage: OWSupportedLanguage? = null
        get() {
            // Initialize cache from current strategy if not yet set
            if (field == null) {
                val currentStrategy = settingsRepository[SharedPrefsItem.LanguageStrategy]
                field = (currentStrategy as? OWLanguageStrategy.Custom)?.language
            }
            return field
        }

    override fun putString(key: String, value: String?) {
        when (key) {
            PreferenceKey.InitialSortOption.key -> {
                val sortOption = if (value == "SERVER") null else value?.let { OWSortOption.valueOf(it) }
                settingsRepository[SharedPrefsItem.InitialSortOption] = sortOption
            }

            PreferenceKey.CommentActionColor.key -> value?.let {
                settingsRepository[SharedPrefsItem.CommentActionColorStyle] = CommentActionsButtonsColor.valueOf(it)
            }

            PreferenceKey.CommentActionFont.key -> value?.let {
                settingsRepository[SharedPrefsItem.CommentActionFontStyle] = CommentActionsButtonsFont.valueOf(it)
            }

            PreferenceKey.FontFamilyStrategy.key ->
                value?.let { settingsRepository[SharedPrefsItem.FontFamilyStrategy] = FontFamilyType.valueOf(it) }

            PreferenceKey.ThemeMode.key -> when (value) {
                "DEFAULT" ->
                    settingsRepository[SharedPrefsItem.SupportSystemDarkMode] = true

                OWThemeMode.LIGHT.name -> {
                    settingsRepository[SharedPrefsItem.SupportSystemDarkMode] = false
                    settingsRepository[SharedPrefsItem.IsDarkMode] = false
                }

                OWThemeMode.DARK.name -> {
                    settingsRepository[SharedPrefsItem.SupportSystemDarkMode] = false
                    settingsRepository[SharedPrefsItem.IsDarkMode] = true
                }
            }

            PreferenceKey.LanguageStrategy.key -> {
                val currentStrategy = settingsRepository[SharedPrefsItem.LanguageStrategy]

                // Cache the current custom language before switching strategies
                if (currentStrategy is OWLanguageStrategy.Custom) {
                    lastCustomLanguage = currentStrategy.language
                }

                val strategy = when (value) {
                    OWLanguageStrategy.Device.toString() -> OWLanguageStrategy.Device
                    OWLanguageStrategy.ServerConfig.toString() -> OWLanguageStrategy.ServerConfig
                    "Custom" -> {
                        // When switching to custom, restore last cached language or default to English
                        OWLanguageStrategy.Custom(lastCustomLanguage ?: OWSupportedLanguage.English)
                    }

                    else -> OWLanguageStrategy.Device
                }
                settingsRepository[SharedPrefsItem.LanguageStrategy] = strategy
            }

            PreferenceKey.CustomLanguage.key -> {
                value?.let { languageName ->
                    val language = OWSupportedLanguage.valueOf(languageName)
                    lastCustomLanguage = language // Cache the selection
                    settingsRepository[SharedPrefsItem.LanguageStrategy] = OWLanguageStrategy.Custom(language)
                }
            }

            PreferenceKey.LocaleStrategy.key -> {
                val strategy = if (value == OWLocaleStrategy.Device.toString()) {
                    OWLocaleStrategy.Device
                } else {
                    OWLocaleStrategy.ServerConfig
                }
                settingsRepository[SharedPrefsItem.LocaleStrategy] = strategy
            }

            PreferenceKey.ArticleStrategy.key ->
                value?.let {
                    settingsRepository[SharedPrefsItem.ArticleInformationStrategy] =
                        ArticleInformationStrategy.valueOf(it)
                }

            PreferenceKey.ArticleUrl.key ->
                settingsRepository[SharedPrefsItem.ArticleAssociatedUrl] = value?.ifEmpty { null }

            PreferenceKey.ArticleSection.key ->
                settingsRepository[SharedPrefsItem.ArticleSection] = value?.ifEmpty { null }

            PreferenceKey.ReadOnlyMode.key ->
                value?.let { settingsRepository[SharedPrefsItem.ReadOnlyMode] = OWReadOnlyMode.valueOf(it) }

            PreferenceKey.PreConvStyle.key -> {
                value?.let {
                    val style = PreConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.PreConversationStyle]
                    settingsRepository[SharedPrefsItem.PreConversationStyle] = currentModel.copy(style = style)
                }
            }

            PreferenceKey.PreConvGuidelinesStyle.key -> {
                value?.let {
                    val style = PreConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.PreConversationStyle]
                    settingsRepository[SharedPrefsItem.PreConversationStyle] = currentModel.copy(
                        communityGuidelinesStyle = style
                    )
                }
            }

            PreferenceKey.PreConvQuestionsStyle.key -> {
                value?.let {
                    val style = PreConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.PreConversationStyle]
                    settingsRepository[SharedPrefsItem.PreConversationStyle] = currentModel.copy(
                        communityQuestionsStyle = style
                    )
                }
            }

            PreferenceKey.ConvStyle.key -> {
                value?.let {
                    val style = ConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(style = style)
                }
            }

            PreferenceKey.ConvGuidelinesStyle.key -> {
                value?.let {
                    val style = ConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        communityGuidelinesStyle = style
                    )
                }
            }

            PreferenceKey.ConvQuestionsStyle.key -> {
                value?.let {
                    val style = ConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        communityQuestionsStyle = style
                    )
                }
            }

            PreferenceKey.ConvSpacingStyle.key -> {
                value?.let {
                    val style = ConversationStyle.valueOf(it)
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        conversationSpacingStyle = style
                    )
                }
            }

            PreferenceKey.ConvSpacingBetweenComments.key -> {
                value?.toIntOrNull()?.let { spacingValue ->
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        betweenCommentsSpacing = spacingValue
                    )
                }
            }

            PreferenceKey.ConvSpacingGuidelines.key -> {
                value?.toIntOrNull()?.let { spacingValue ->
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        communityGuidelinesSpacing = spacingValue
                    )
                }
            }

            PreferenceKey.ConvSpacingQuestions.key -> {
                value?.toIntOrNull()?.let { spacingValue ->
                    val currentModel = settingsRepository[SharedPrefsItem.ConversationStyle]
                    settingsRepository[SharedPrefsItem.ConversationStyle] = currentModel.copy(
                        communityQuestionsSpacing = spacingValue
                    )
                }
            }

            PreferenceKey.CommentCreationStyle.key -> {
                value?.let {
                    val style = when (it) {
                        OWCommentCreationStyle.Regular.toString() -> OWCommentCreationStyle.Regular
                        OWCommentCreationStyle.Light.toString() -> OWCommentCreationStyle.Light
                        OWCommentCreationStyle.Floating.toString() -> OWCommentCreationStyle.Floating
                        else -> OWCommentCreationStyle.Regular
                    }
                    settingsRepository[SharedPrefsItem.CommentCreationStyle] = style
                }
            }

            PreferenceKey.CommentThreadId.key ->
                settingsRepository[SharedPrefsItem.CommentThreadId] = value?.ifEmpty { null }

            PreferenceKey.Environment.key -> {
                // Don't persist environment changes here - they will be persisted when Apply button is clicked
                // This allows users to select different environments without immediately persisting the change
            }

            PreferenceKey.EnvironmentBaseUrl.key ->
                settingsRepository[SharedPrefsItem.EnvironmentBaseUrl] = value?.ifEmpty { null }

            PreferenceKey.MockSsoEnvironment.key ->
                value?.let { settingsRepository[SharedPrefsItem.MockSSOEnvironment] = OWEnvironment.valueOf(it) }

            PreferenceKey.ProfileUrlPath.key ->
                settingsRepository[SharedPrefsItem.ProfileUrlPath] = value?.ifEmpty { null }
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        when (key) {
            PreferenceKey.ShowLoginPrompt.key -> settingsRepository[SharedPrefsItem.ShowLoginPrompt] = value
            PreferenceKey.EnableLandscape.key -> settingsRepository[SharedPrefsItem.EnableLandScape] = value
            PreferenceKey.HideArticleHeader.key -> settingsRepository[SharedPrefsItem.ArticleHeaderStyle] = value
            PreferenceKey.EnableSocialReviews.key -> settingsRepository[SharedPrefsItem.EnableSocialReviews] = value
            PreferenceKey.EnablePullToRefresh.key -> settingsRepository[SharedPrefsItem.EnablePullToRefresh] = value
            PreferenceKey.EnableLoginDelegation.key -> settingsRepository[SharedPrefsItem.EnableLoginDelegation] = value
            PreferenceKey.LoggerView.key -> settingsRepository[SharedPrefsItem.ShowLogger] = value
            PreferenceKey.EnableCustomUIDelegation.key ->
                settingsRepository[SharedPrefsItem.EnableCustomUIDelegation] = value
        }
    }

    override fun putInt(key: String, value: Int) {
        when (key) {
            PreferenceKey.PreConvNumComments.key -> {
                val currentModel = settingsRepository[SharedPrefsItem.PreConversationStyle]
                settingsRepository[SharedPrefsItem.PreConversationStyle] = currentModel.copy(numberOfComments = value)
            }
        }
    }

    override fun getInt(key: String, defValue: Int): Int =
        if (key == PreferenceKey.PreConvNumComments.key) {
            settingsRepository[SharedPrefsItem.PreConversationStyle].numberOfComments
        } else {
            defValue
        }

    override fun getString(key: String, defValue: String?): String? =
        when (key) {
            PreferenceKey.InitialSortOption.key ->
                settingsRepository[SharedPrefsItem.InitialSortOption]?.name ?: "SERVER"

            PreferenceKey.CommentActionColor.key -> settingsRepository[SharedPrefsItem.CommentActionColorStyle].name
            PreferenceKey.CommentActionFont.key -> settingsRepository[SharedPrefsItem.CommentActionFontStyle].name
            PreferenceKey.FontFamilyStrategy.key -> settingsRepository[SharedPrefsItem.FontFamilyStrategy].toString()

            PreferenceKey.ThemeMode.key -> {
                val isDarkMode = settingsRepository[SharedPrefsItem.IsDarkMode]
                val supportSystemDarkMode = settingsRepository[SharedPrefsItem.SupportSystemDarkMode]
                if (supportSystemDarkMode) {
                    "DEFAULT"
                } else {
                    if (isDarkMode) OWThemeMode.DARK.name else OWThemeMode.LIGHT.name
                }
            }

            PreferenceKey.LanguageStrategy.key ->
                when (settingsRepository[SharedPrefsItem.LanguageStrategy]) {
                    is OWLanguageStrategy.Device -> OWLanguageStrategy.Device.toString()
                    is OWLanguageStrategy.ServerConfig -> OWLanguageStrategy.ServerConfig.toString()
                    is OWLanguageStrategy.Custom -> "Custom"
                }

            PreferenceKey.CustomLanguage.key -> {
                val strategy = settingsRepository[SharedPrefsItem.LanguageStrategy]
                if (strategy is OWLanguageStrategy.Custom) {
                    strategy.language.name
                } else {
                    OWSupportedLanguage.English.name
                }
            }

            PreferenceKey.LocaleStrategy.key -> {
                val strategy = settingsRepository[SharedPrefsItem.LocaleStrategy]
                if (strategy is OWLocaleStrategy.Device) {
                    OWLocaleStrategy.Device.toString()
                } else {
                    OWLocaleStrategy.ServerConfig.toString()
                }
            }

            PreferenceKey.ArticleStrategy.key ->
                settingsRepository[SharedPrefsItem.ArticleInformationStrategy].toString()

            PreferenceKey.ArticleUrl.key -> settingsRepository[SharedPrefsItem.ArticleAssociatedUrl]
            PreferenceKey.ArticleSection.key -> settingsRepository[SharedPrefsItem.ArticleSection]
            PreferenceKey.ReadOnlyMode.key -> settingsRepository[SharedPrefsItem.ReadOnlyMode].name
            PreferenceKey.PreConvStyle.key -> settingsRepository[SharedPrefsItem.PreConversationStyle].style.toString()

            PreferenceKey.PreConvGuidelinesStyle.key ->
                settingsRepository[SharedPrefsItem.PreConversationStyle].communityGuidelinesStyle.toString()

            PreferenceKey.PreConvQuestionsStyle.key ->
                settingsRepository[SharedPrefsItem.PreConversationStyle].communityQuestionsStyle.toString()

            PreferenceKey.ConvStyle.key -> settingsRepository[SharedPrefsItem.ConversationStyle].style.toString()

            PreferenceKey.ConvGuidelinesStyle.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].communityGuidelinesStyle.toString()

            PreferenceKey.ConvQuestionsStyle.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].communityQuestionsStyle.toString()

            PreferenceKey.ConvSpacingStyle.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].conversationSpacingStyle.toString()

            PreferenceKey.ConvSpacingBetweenComments.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].betweenCommentsSpacing.toString()

            PreferenceKey.ConvSpacingGuidelines.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].communityGuidelinesSpacing.toString()

            PreferenceKey.ConvSpacingQuestions.key ->
                settingsRepository[SharedPrefsItem.ConversationStyle].communityQuestionsSpacing.toString()

            PreferenceKey.CommentCreationStyle.key ->
                settingsRepository[SharedPrefsItem.CommentCreationStyle].toString()

            PreferenceKey.CommentThreadId.key -> settingsRepository[SharedPrefsItem.CommentThreadId]
            PreferenceKey.Environment.key -> settingsRepository[SharedPrefsItem.Environment].name
            PreferenceKey.EnvironmentBaseUrl.key -> settingsRepository[SharedPrefsItem.EnvironmentBaseUrl]
            PreferenceKey.MockSsoEnvironment.key -> settingsRepository[SharedPrefsItem.MockSSOEnvironment].name
            PreferenceKey.ProfileUrlPath.key -> settingsRepository[SharedPrefsItem.ProfileUrlPath]
            else -> defValue
        }

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        when (key) {
            PreferenceKey.ShowLoginPrompt.key -> settingsRepository[SharedPrefsItem.ShowLoginPrompt]
            PreferenceKey.EnableLandscape.key -> settingsRepository[SharedPrefsItem.EnableLandScape]
            PreferenceKey.HideArticleHeader.key -> settingsRepository[SharedPrefsItem.ArticleHeaderStyle]
            PreferenceKey.EnableSocialReviews.key -> settingsRepository[SharedPrefsItem.EnableSocialReviews]
            PreferenceKey.EnablePullToRefresh.key -> settingsRepository[SharedPrefsItem.EnablePullToRefresh]
            PreferenceKey.EnableLoginDelegation.key -> settingsRepository[SharedPrefsItem.EnableLoginDelegation]
            PreferenceKey.LoggerView.key -> settingsRepository[SharedPrefsItem.ShowLogger]
            PreferenceKey.EnableCustomUIDelegation.key -> settingsRepository[SharedPrefsItem.EnableCustomUIDelegation]
            else -> defValue
        }
}
