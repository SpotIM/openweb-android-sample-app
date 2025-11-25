package openweb.sample.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import openweb.sample.ui.model.ConversationSettingsModel
import openweb.sample.ui.model.PreConversationSettingsModel
import openweb.sample.ui.screens.settings.customtheme.CustomThemeSetting
import openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy
import openweb.sample.ui.screens.settings.enums.FontFamilyType
import openweb.sample.utils.fromJson
import openweb.sample.utils.fromString
import openweb.sample.utils.logger.SampleLogger
import openweb.sample.utils.toJson
import openweb.sample.utils.valueOf
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

/**
 * Class to manage shared preferences operations.
 */
class SharedPreferencesManager(context: Context) : SharedPreferencesOperations {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private inline fun <reified T> setValue(key: String, value: T?) {
        SampleLogger.d("Setting value for key: $key, value: $value")
        sharedPreferences.edit().apply {
            if (value == null) {
                remove(key)
            } else {
                val jsonString = json.encodeToString(value)
                putString(key, jsonString)
                SampleLogger.d("Saving value for key: $key, value: $value")
                SampleLogger.d("Saving value for key: $key, value: $jsonString")
            }
            apply()
        }
    }

    private inline fun <reified T> getValue(key: String, defaultValue: T? = null): T? {
        return try {
            val jsonString = sharedPreferences.getString(key, null)
            val value = if (jsonString != null) json.decodeFromString<T>(jsonString) else defaultValue
            SampleLogger.d("Getting value for key: $key, value: $value")
            value
        } catch (e: Exception) {
            SampleLogger.e("Error getting value for key: $key", e = e)
            defaultValue
        }
    }

    override fun setCustomThemeColorsList(themeColorsList: List<CustomThemeSetting>?) = setValue(
        SharedPrefsItem.CustomThemeColors.key,
        themeColorsList
    )

    override fun getCustomThemeColorsList() = getValue<ArrayList<CustomThemeSetting>>(
        SharedPrefsItem.CustomThemeColors.key
    )

    override fun setCommentActionsButtonsFont(commentActionsButtonsFont: CommentActionsButtonsFont) = setValue(
        SharedPrefsItem.CommentActionFontStyle.key,
        commentActionsButtonsFont
    )

    override fun getCommentActionsButtonsFont() = getValue<CommentActionsButtonsFont>(
        SharedPrefsItem.CommentActionFontStyle.key
    )

    override fun setCommentActionsButtonsColor(commentActionButtonsColor: CommentActionsButtonsColor) = setValue(
        SharedPrefsItem.CommentActionColorStyle.key,
        commentActionButtonsColor
    )

    override fun getCommentActionsButtonsColor() = getValue<CommentActionsButtonsColor>(
        SharedPrefsItem.CommentActionColorStyle.key
    )

    override fun getArticleAssociatedUrl() = getValue<String>(SharedPrefsItem.ArticleAssociatedUrl.key)

    override fun setArticleAssociatedUrl(url: String?) = setValue<String>(SharedPrefsItem.ArticleAssociatedUrl.key, url)

    override fun getArticleHeaderStyle() = getValue<Boolean>(SharedPrefsItem.ArticleHeaderStyle.key)
        ?: SharedPrefsItem.ArticleHeaderStyle.defaultValue

    override fun setArticleHeaderStyle(style: Boolean) = setValue<Boolean>(
        SharedPrefsItem.ArticleHeaderStyle.key,
        style
    )

    override fun getArticleInformationStrategy() = getValue<ArticleInformationStrategy>(
        SharedPrefsItem.ArticleInformationStrategy.key
    )
        ?: SharedPrefsItem.ArticleInformationStrategy.defaultValue

    override fun setArticleInformationStrategy(strategy: ArticleInformationStrategy) =
        setValue<ArticleInformationStrategy>(SharedPrefsItem.ArticleInformationStrategy.key, strategy)

    override fun getArticleSection() = getValue<String>(SharedPrefsItem.ArticleSection.key)

    override fun setArticleSection(section: String?) {
        setValue(SharedPrefsItem.ArticleSection.key, section)
    }

    override fun getEnableSocialReviews() = getValue<Boolean>(SharedPrefsItem.EnableSocialReviews.key)
        ?: SharedPrefsItem.EnableSocialReviews.defaultValue

    override fun setEnableSocialReviews(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.EnableSocialReviews.key,
        enable
    )

    override fun getCommentCreationStyle(): OWCommentCreationStyle {
        val value = getValue<String>(SharedPrefsItem.CommentCreationStyle.key)
        val style = value?.let { OWCommentCreationStyle.fromString(it) }
        return style ?: SharedPrefsItem.CommentCreationStyle.defaultValue
    }

    override fun setCommentCreationStyle(style: OWCommentCreationStyle) {
        setValue<String>(SharedPrefsItem.CommentCreationStyle.key, style.toString())
    }

    override fun getCommentThreadId(): String? = getValue<String>(SharedPrefsItem.CommentThreadId.key)

    override fun setCommentThreadId(threadId: String?) = setValue<String>(SharedPrefsItem.CommentThreadId.key, threadId)

    override fun getConversationStyle(): ConversationSettingsModel? = getValue<String>(
        SharedPrefsItem.ConversationStyle.key
    )?.fromJson()

    override fun setConversationStyle(style: ConversationSettingsModel) = setValue<String>(
        SharedPrefsItem.ConversationStyle.key,
        style.toJson()
    )

    override fun getCustomDarkColor() = getValue<Int>(SharedPrefsItem.CustomDarkColor.key)

    override fun setCustomDarkColor(color: Int?) = setValue<Int>(SharedPrefsItem.CustomDarkColor.key, color)

    override fun getEnableLandScape() = getValue<Boolean>(SharedPrefsItem.EnableLandScape.key)

    override fun setEnableLandScape(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.EnableLandScape.key,
        enable
    )

    override fun getShowLogger() = getValue<Boolean>(SharedPrefsItem.ShowLogger.key)
        ?: SharedPrefsItem.ShowLogger.defaultValue

    override fun setShowLogger(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.ShowLogger.key,
        enable
    )

    override fun getIsDarkMode(): Boolean = getValue<Boolean>(SharedPrefsItem.IsDarkMode.key)
        ?: SharedPrefsItem.IsDarkMode.defaultValue

    override fun setIsDarkMode(isDarkMode: Boolean) = setValue<Boolean>(SharedPrefsItem.IsDarkMode.key, isDarkMode)

    override fun setFontFamilyStrategy(strategy: FontFamilyType) {
        setValue<String>(SharedPrefsItem.FontFamilyStrategy.key, strategy.toString())
    }

    override fun getFontFamilyStrategy(): FontFamilyType {
        val value = getValue<String>(SharedPrefsItem.FontFamilyStrategy.key)
        return value?.let { FontFamilyType.valueOf(it) } ?: FontFamilyType.Default
    }

    override fun getLanguageStrategy(): OWLanguageStrategy? {
        val value = getValue<String>(SharedPrefsItem.LanguageStrategy.key)
        return value?.let { OWLanguageStrategy.Companion.fromString(it) }
            ?: SharedPrefsItem.LanguageStrategy.defaultValue
    }

    override fun setLanguageStrategy(strategy: OWLanguageStrategy) {
        setValue<String>(SharedPrefsItem.LanguageStrategy.key, strategy.toString())
    }

    override fun getLocaleStrategy(): OWLocaleStrategy? {
        val value = getValue<String>(SharedPrefsItem.LocaleStrategy.key)
        return value?.let { OWLocaleStrategy.valueOf(it) } ?: SharedPrefsItem.LocaleStrategy.defaultValue
    }

    override fun setLocaleStrategy(strategy: OWLocaleStrategy) = setValue<String>(
        SharedPrefsItem.LocaleStrategy.key,
        strategy.toString()
    )

    override fun getPostId(): OWPostId? = getValue<OWPostId>(SharedPrefsItem.PostId.key)

    override fun setPostId(postId: OWPostId?) {
        setValue<OWPostId>(SharedPrefsItem.PostId.key, postId)
    }

    override fun getPreConversationStyle(): PreConversationSettingsModel? = getValue<String>(
        SharedPrefsItem.PreConversationStyle.key
    )?.fromJson()

    override fun setPreConversationStyle(style: PreConversationSettingsModel) = setValue<String>(
        SharedPrefsItem.PreConversationStyle.key,
        style.toJson()
    )

    override fun getReadOnlyMode(): OWReadOnlyMode? = getValue<String>(SharedPrefsItem.ReadOnlyMode.key)?.fromJson()

    override fun setReadOnlyMode(mode: OWReadOnlyMode) = setValue<String>(
        SharedPrefsItem.ReadOnlyMode.key,
        mode.toJson()
    )

    override fun getSpotId(): OWSpotId? = getValue<OWSpotId>(SharedPrefsItem.SpotId.key)

    override fun setSpotId(spotId: OWSpotId?) {
        setValue<OWSpotId>(SharedPrefsItem.SpotId.key, spotId)
    }

    override fun getSupportSystemDarkMode() = getValue<Boolean>(SharedPrefsItem.SupportSystemDarkMode.key)
        ?: SharedPrefsItem.SupportSystemDarkMode.defaultValue

    override fun setSupportSystemDarkMode(support: Boolean) = setValue<Boolean>(
        SharedPrefsItem.SupportSystemDarkMode.key,
        support
    )

    override fun getSupportedLanguage(): OWSupportedLanguage? {
        val value = getValue<String>(SharedPrefsItem.SupportedLanguage.key)
        return value?.let { OWSupportedLanguage.valueOf(it) } ?: SharedPrefsItem.SupportedLanguage.defaultValue
    }

    override fun setSupportedLanguage(language: OWSupportedLanguage) = setValue<String>(
        SharedPrefsItem.SupportedLanguage.key,
        language.toString()
    )

    override fun getEnvironmentType(): OWEnvironment? {
        val value = getValue<String>(SharedPrefsItem.Environment.key)
        return value?.let { OWEnvironment.valueOf(it) }
    }

    override fun setEnvironmentType(environmentType: OWEnvironment) =
        setValue<String>(SharedPrefsItem.Environment.key, environmentType.toString())

    override fun getEnvironmentBaseUrl() = getValue<String>(SharedPrefsItem.EnvironmentBaseUrl.key)

    override fun setEnvironmentBaseUrl(url: String?) = setValue<String>(
        SharedPrefsItem.EnvironmentBaseUrl.key,
        url
    )

    override fun getMockSSOEnvironment(): OWEnvironment? {
        val value = getValue<String>(SharedPrefsItem.MockSSOEnvironment.key)
        return value?.let { OWEnvironment.valueOf(it) }
    }

    override fun setMockSSOEnvironment(environment: OWEnvironment) =
        setValue<String>(SharedPrefsItem.MockSSOEnvironment.key, environment.toString())

    override fun getShowLoginPrompt() = getValue<Boolean>(SharedPrefsItem.ShowLoginPrompt.key)

    override fun setShowLoginPrompt(showLoginPrompt: Boolean) = setValue<Boolean>(
        SharedPrefsItem.ShowLoginPrompt.key,
        showLoginPrompt
    )

    override fun getEnableLoginDelegation() = getValue<Boolean>(SharedPrefsItem.EnableLoginDelegation.key)

    override fun setEnableLoginDelegation(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.EnableLoginDelegation.key,
        enable
    )

    override fun getEnableCustomUIDelegation(): Boolean =
        getValue<Boolean>(SharedPrefsItem.EnableCustomUIDelegation.key)
            ?: SharedPrefsItem.EnableCustomUIDelegation.defaultValue

    override fun setEnableCustomUIDelegation(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.EnableCustomUIDelegation.key,
        enable
    )

    override fun getEndpointOverrides(): List<EndpointOverride> {
        val jsonString = getValue<String>(SharedPrefsItem.EndpointOverrides.key)
        return jsonString?.let { json.decodeFromString<List<EndpointOverride>>(it) }
            ?: emptyList()
    }

    override fun setEndpointOverrides(overrides: List<EndpointOverride>?) {
        setValue<String>(
            SharedPrefsItem.EndpointOverrides.key,
            overrides?.let { json.encodeToString(it) }
        )
    }

    override fun getInitialSortOption(): OWSortOption? {
        val value = getValue<String>(SharedPrefsItem.InitialSortOption.key)
        return value?.let { OWSortOption.valueOf(it) }
    }

    override fun setInitialSortOption(sortOption: OWSortOption?) = setValue<String>(
        SharedPrefsItem.InitialSortOption.key,
        sortOption?.toString()
    )

    override fun getEnablePullToRefresh(): Boolean = getValue<Boolean>(SharedPrefsItem.EnablePullToRefresh.key)
        ?: SharedPrefsItem.EnablePullToRefresh.defaultValue

    override fun setEnablePullToRefresh(enable: Boolean) = setValue<Boolean>(
        SharedPrefsItem.EnablePullToRefresh.key,
        enable
    )

    override fun getProfileUrlPath(): String? = getValue<String>(SharedPrefsItem.ProfileUrlPath.key)

    override fun setProfileUrlPath(urlPath: String?) = setValue<String>(
        SharedPrefsItem.ProfileUrlPath.key,
        urlPath
    )

    fun <T> getItem(item: SharedPrefsItem<T>): T? {
        return when (item) {
            is SharedPrefsItem.ArticleAssociatedUrl -> getArticleAssociatedUrl() as T
            is SharedPrefsItem.ArticleHeaderStyle -> getArticleHeaderStyle() as T
            is SharedPrefsItem.ArticleInformationStrategy -> getArticleInformationStrategy() as T
            is SharedPrefsItem.ArticleSection -> getArticleSection() as T
            is SharedPrefsItem.EnableSocialReviews -> getEnableSocialReviews() as T
            is SharedPrefsItem.CommentActionColorStyle -> getCommentActionsButtonsColor() as T
            is SharedPrefsItem.CommentActionFontStyle -> getCommentActionsButtonsFont() as T
            is SharedPrefsItem.CommentCreationStyle -> getCommentCreationStyle() as T
            is SharedPrefsItem.CommentThreadId -> getCommentThreadId() as T
            is SharedPrefsItem.ConversationStyle -> getConversationStyle() as T
            is SharedPrefsItem.CustomDarkColor -> getCustomDarkColor() as T
            is SharedPrefsItem.CustomThemeColors -> getCustomThemeColorsList() as T
            is SharedPrefsItem.EnableLandScape -> getEnableLandScape() as T
            is SharedPrefsItem.ShowLogger -> getShowLogger() as T
            is SharedPrefsItem.Environment -> getEnvironmentType() as T
            is SharedPrefsItem.EnvironmentBaseUrl -> getEnvironmentBaseUrl() as T
            is SharedPrefsItem.FontFamilyStrategy -> getFontFamilyStrategy() as T
            is SharedPrefsItem.IsDarkMode -> getIsDarkMode() as T
            is SharedPrefsItem.LanguageStrategy -> getLanguageStrategy() as T
            is SharedPrefsItem.LocaleStrategy -> getLocaleStrategy() as T
            is SharedPrefsItem.PostId -> getPostId() as T
            is SharedPrefsItem.PreConversationStyle -> getPreConversationStyle() as T
            is SharedPrefsItem.ReadOnlyMode -> getReadOnlyMode() as T
            is SharedPrefsItem.SpotId -> getSpotId() as T
            is SharedPrefsItem.SupportSystemDarkMode -> getSupportSystemDarkMode() as T
            is SharedPrefsItem.SupportedLanguage -> getSupportedLanguage() as T
            is SharedPrefsItem.ShowLoginPrompt -> getShowLoginPrompt() as T
            is SharedPrefsItem.EnableLoginDelegation -> getEnableLoginDelegation() as T
            is SharedPrefsItem.EnableCustomUIDelegation -> getEnableCustomUIDelegation() as T
            is SharedPrefsItem.EndpointOverrides -> getEndpointOverrides() as T
            is SharedPrefsItem.InitialSortOption -> getInitialSortOption() as T
            is SharedPrefsItem.EnablePullToRefresh -> getEnablePullToRefresh() as T
            is SharedPrefsItem.ProfileUrlPath -> getProfileUrlPath() as T
            is SharedPrefsItem.MockSSOEnvironment -> getMockSSOEnvironment() as T
        }
    }

    fun <T> setItem(item: SharedPrefsItem<T>, value: T) {
        when (item) {
            is SharedPrefsItem.ArticleAssociatedUrl -> setArticleAssociatedUrl(value as? String)
            is SharedPrefsItem.ArticleHeaderStyle -> setArticleHeaderStyle(value as Boolean)
            is SharedPrefsItem.ArticleInformationStrategy -> setArticleInformationStrategy(
                value as ArticleInformationStrategy
            )

            is SharedPrefsItem.ArticleSection -> setArticleSection(value as? String)
            is SharedPrefsItem.EnableSocialReviews -> setEnableSocialReviews(value as Boolean)
            is SharedPrefsItem.CommentActionColorStyle -> setCommentActionsButtonsColor(
                value as CommentActionsButtonsColor
            )

            is SharedPrefsItem.CommentActionFontStyle -> setCommentActionsButtonsFont(
                value as CommentActionsButtonsFont
            )

            is SharedPrefsItem.CommentCreationStyle -> setCommentCreationStyle(value as OWCommentCreationStyle)
            is SharedPrefsItem.CommentThreadId -> setCommentThreadId(value as? String)
            is SharedPrefsItem.ConversationStyle -> setConversationStyle(value as ConversationSettingsModel)
            is SharedPrefsItem.CustomDarkColor -> setCustomDarkColor(value as? Int)
            is SharedPrefsItem.CustomThemeColors -> setCustomThemeColorsList(value as? List<CustomThemeSetting>)
            is SharedPrefsItem.EnableLandScape -> setEnableLandScape(value as Boolean)
            is SharedPrefsItem.ShowLogger -> setShowLogger(value as Boolean)
            is SharedPrefsItem.Environment -> setEnvironmentType(value as OWEnvironment)
            is SharedPrefsItem.EnvironmentBaseUrl -> setEnvironmentBaseUrl(value as? String)
            is SharedPrefsItem.FontFamilyStrategy -> setFontFamilyStrategy(value as FontFamilyType)
            is SharedPrefsItem.IsDarkMode -> setIsDarkMode(value as Boolean)
            is SharedPrefsItem.LanguageStrategy -> setLanguageStrategy(value as OWLanguageStrategy)
            is SharedPrefsItem.LocaleStrategy -> setLocaleStrategy(value as OWLocaleStrategy)
            is SharedPrefsItem.PostId -> setPostId(value as? OWPostId)
            is SharedPrefsItem.PreConversationStyle -> setPreConversationStyle(value as PreConversationSettingsModel)
            is SharedPrefsItem.ReadOnlyMode -> setReadOnlyMode(value as OWReadOnlyMode)
            is SharedPrefsItem.SpotId -> setSpotId(value as? OWSpotId)
            is SharedPrefsItem.SupportSystemDarkMode -> setSupportSystemDarkMode(value as Boolean)
            is SharedPrefsItem.SupportedLanguage -> setSupportedLanguage(value as OWSupportedLanguage)
            is SharedPrefsItem.ShowLoginPrompt -> setShowLoginPrompt(value as Boolean)
            is SharedPrefsItem.EnableLoginDelegation -> setEnableLoginDelegation(value as Boolean)
            is SharedPrefsItem.EnableCustomUIDelegation -> setEnableCustomUIDelegation(value as Boolean)
            is SharedPrefsItem.EndpointOverrides -> setEndpointOverrides(value as? List<EndpointOverride>)
            is SharedPrefsItem.InitialSortOption -> setInitialSortOption(value as? OWSortOption)
            is SharedPrefsItem.EnablePullToRefresh -> setEnablePullToRefresh(value as Boolean)
            is SharedPrefsItem.ProfileUrlPath -> setProfileUrlPath(value as? String)
            is SharedPrefsItem.MockSSOEnvironment -> setMockSSOEnvironment(value as OWEnvironment)
        }
    }

    fun clearAllSettings(keysToPreserve: List<String>): Boolean {
        val needsRestart = getEnvironmentType()?.let { it != SharedPrefsItem.Environment.defaultValue } ?: false

        val allValues = sharedPreferences.all
        val preservedValues = keysToPreserve.mapNotNull { key ->
            allValues[key]?.let { key to it }
        }.toMap()

        sharedPreferences.edit(commit = true) {
            clear()
            preservedValues.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Set<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        putStringSet(key, value as Set<String>)
                    }
                }
            }
        }

        return needsRestart
    }

    companion object {
        private const val DEFAULT_FILE_NAME = "app_prefs"
    }
}
