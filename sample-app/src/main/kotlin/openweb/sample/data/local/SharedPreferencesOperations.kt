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
import openweb.sample.ui.model.ConversationSettingsModel
import openweb.sample.ui.model.PreConversationSettingsModel
import openweb.sample.ui.screens.settings.customelement.CustomElementSetting
import openweb.sample.ui.screens.settings.customfont.CustomFontSetting
import openweb.sample.ui.screens.settings.customtheme.CombinedThemeColorSetting
import openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy
import openweb.sample.ui.screens.settings.enums.FontFamilyType

/**
 * Interface for operations related to SharedPreferences.
 */
interface SharedPreferencesOperations {
    fun setCustomFontElements(fontSettings: List<CustomFontSetting>?)
    fun getCustomFontElements(): List<CustomFontSetting>?

    fun setCustomElementToggles(settings: List<CustomElementSetting>?)
    fun getCustomElementToggles(): List<CustomElementSetting>?

    fun setCombinedThemeColors(combinedColors: List<CombinedThemeColorSetting>?)
    fun getCombinedThemeColors(): ArrayList<CombinedThemeColorSetting>?

    fun setCommentActionsButtonsFont(commentActionsButtonsFont: CommentActionsButtonsFont)
    fun getCommentActionsButtonsFont(): CommentActionsButtonsFont?

    fun setCommentActionsButtonsColor(commentActionButtonsColor: CommentActionsButtonsColor)
    fun getCommentActionsButtonsColor(): CommentActionsButtonsColor?

    fun setArticleAssociatedUrl(url: String?)
    fun getArticleAssociatedUrl(): String?

    fun setArticleHeaderStyle(style: Boolean)
    fun getArticleHeaderStyle(): Boolean

    fun setArticleInformationStrategy(strategy: ArticleInformationStrategy)
    fun getArticleInformationStrategy(): ArticleInformationStrategy?

    fun setArticleSection(section: String?)
    fun getArticleSection(): String?

    fun setEnableSocialReviews(enable: Boolean)
    fun getEnableSocialReviews(): Boolean?

    fun setCommentCreationStyle(style: OWCommentCreationStyle)
    fun getCommentCreationStyle(): OWCommentCreationStyle

    fun setCommentThreadId(threadId: String?)
    fun getCommentThreadId(): String?

    fun setConversationStyle(style: ConversationSettingsModel)
    fun getConversationStyle(): ConversationSettingsModel?

    fun setCustomDarkColor(color: Int?)
    fun getCustomDarkColor(): Int?

    fun setEnableLandScape(enable: Boolean)
    fun getEnableLandScape(): Boolean?

    fun setShowLogger(enable: Boolean)
    fun getShowLogger(): Boolean?

    fun setEnvironmentType(environmentType: OWEnvironment)
    fun getEnvironmentType(): OWEnvironment?

    fun setEnvironmentBaseUrl(url: String?)
    fun getEnvironmentBaseUrl(): String?

    fun setMockSSOEnvironment(environment: OWEnvironment)
    fun getMockSSOEnvironment(): OWEnvironment?

    fun setIsDarkMode(isDarkMode: Boolean)
    fun getIsDarkMode(): Boolean

    fun setFontFamilyStrategy(strategy: FontFamilyType)
    fun getFontFamilyStrategy(): FontFamilyType?

    fun setLanguageStrategy(strategy: OWLanguageStrategy)
    fun getLanguageStrategy(): OWLanguageStrategy?

    fun setLocaleStrategy(strategy: OWLocaleStrategy)
    fun getLocaleStrategy(): OWLocaleStrategy?

    fun setPostId(postId: OWPostId?)
    fun getPostId(): String?

    fun setPreConversationStyle(style: PreConversationSettingsModel)
    fun getPreConversationStyle(): PreConversationSettingsModel?

    fun setReadOnlyMode(mode: OWReadOnlyMode)
    fun getReadOnlyMode(): OWReadOnlyMode?

    fun setSpotId(spotId: OWSpotId?)
    fun getSpotId(): OWSpotId?

    fun setSupportSystemDarkMode(support: Boolean)
    fun getSupportSystemDarkMode(): Boolean?

    fun setSupportedLanguage(language: OWSupportedLanguage)
    fun getSupportedLanguage(): OWSupportedLanguage?

    fun setShowLoginPrompt(show: Boolean)
    fun getShowLoginPrompt(): Boolean?

    fun setEnableLoginDelegation(enable: Boolean)
    fun getEnableLoginDelegation(): Boolean?

    fun setEndpointOverrides(overrides: List<EndpointOverride>?)
    fun getEndpointOverrides(): List<EndpointOverride>

    fun setInitialSortOption(sortOption: OWSortOption?)
    fun getInitialSortOption(): OWSortOption?

    fun setEnablePullToRefresh(enable: Boolean)
    fun getEnablePullToRefresh(): Boolean

    fun setConversationNavToolbarStyle(style: String)
    fun getConversationNavToolbarStyle(): String

    fun setCommentThreadNavToolbarStyle(style: String)
    fun getCommentThreadNavToolbarStyle(): String

    fun setCommentThreadNavToolbarShowTitle(show: Boolean)
    fun getCommentThreadNavToolbarShowTitle(): Boolean

    fun setCommentThreadNavBackButtonStyle(style: String)
    fun getCommentThreadNavBackButtonStyle(): String

    fun setNavToolbarShowTitle(show: Boolean)
    fun getNavToolbarShowTitle(): Boolean

    fun setNavBackButtonStyle(style: String)
    fun getNavBackButtonStyle(): String

    fun setPreConversationHeaderStyle(style: String)
    fun getPreConversationHeaderStyle(): String

    fun getProfileUrlPath(): String?
    fun setProfileUrlPath(urlPath: String?)

    fun setEnableReactions(enable: Boolean)
    fun getEnableReactions(): Boolean

    fun setReactionsThemeName(themeName: String)
    fun getReactionsThemeName(): String

    fun setSkipRenewSSOImplementation(skip: Boolean)
    fun getSkipRenewSSOImplementation(): Boolean
}
