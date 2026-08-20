package openweb.sample.utils

/**
 * Type-safe preference keys using sealed class hierarchy.
 * Provides compile-time safety and prevents typos in preference key strings.
 *
 * Note: While this lives in the UI layer, it's acceptable for the data layer
 * (SettingsPreferenceDataStore) to reference these keys since they're just
 * infrastructure constants with no business logic or UI dependencies.
 */
sealed class PreferenceKey(val key: String) {

    // App Data / Presets
    data object SpotId : PreferenceKey("spotId")
    data object PostId : PreferenceKey("postId")

    // Last successful SSO login, persisted so the headless renew authenticator can replay it.
    data object LastSsoLogin : PreferenceKey("last_sso_login")

    // Root menu navigation
    data object Customizations : PreferenceKey("customizations")
    data object Authentication : PreferenceKey("authentication")
    data object Configurations : PreferenceKey("configurations")
    data object ArticleSettings : PreferenceKey("article_settings")
    data object ScreensSettings : PreferenceKey("screens_settings")
    data object InternalSettings : PreferenceKey("internal_settings")

    // Customizations
    data object CustomDarkColor : PreferenceKey("custom_dark_color")
    data object CustomElementColors : PreferenceKey("custom_element_colors")
    data object CombinedThemeColors : PreferenceKey("combined_theme_colors")
    // Keys below are used as storage keys by SharedPrefsItem (data layer); no longer shown as preferences
    data object CustomFontElements : PreferenceKey("custom_font_elements")
    data object CustomElementCustomization : PreferenceKey("custom_element_customization")
    data object ElementCustomizations : PreferenceKey("element_customizations")
    data object InitialSortOption : PreferenceKey("initial_sort_option")
    data object CommentActionColor : PreferenceKey("comment_action_color_style")
    data object CommentActionFont : PreferenceKey("comment_action_font_style")
    data object FontFamilyStrategy : PreferenceKey("font_family_strategy")
    data object ThemeMode : PreferenceKey("theme_mode")
    data object IsDarkMode : PreferenceKey("darkMode")
    data object SupportSystemDarkMode : PreferenceKey("supportSystemDarkMode")
    // Configurations
    data object LanguageStrategy : PreferenceKey("language_strategy")
    data object CustomLanguage : PreferenceKey("custom_language")
    data object LocaleStrategy : PreferenceKey("locale_strategy")
    data object EnableLandscape : PreferenceKey("enable_landscape")

    // Article
    data object ArticleStrategy : PreferenceKey("article_information_strategy")
    data object ArticleUrl : PreferenceKey("article_associated_url")
    data object ArticleSection : PreferenceKey("article_section")
    data object ReadOnlyMode : PreferenceKey("read_only_mode")

    // Screens - PreConversation
    data object PreConvStyle : PreferenceKey("pre_conversation_style")
    data object PreConvNumComments : PreferenceKey("pre_conversation_num_comments")
    data object PreConvGuidelinesStyle : PreferenceKey("pre_conversation_guidelines_style")
    data object PreConvQuestionsStyle : PreferenceKey("pre_conversation_questions_style")
    data object PreConvHeaderStyle : PreferenceKey("pre_conversation_header_style")
    data object PreConversationSettings : PreferenceKey("preConversationSettings")

    // Screens - Conversation
    data object ConvStyle : PreferenceKey("conversation_style")
    data object ConvGuidelinesStyle : PreferenceKey("conversation_guidelines_style")
    data object ConvQuestionsStyle : PreferenceKey("conversation_questions_style")
    data object ConvNavToolbarStyle : PreferenceKey("conversation_nav_toolbar_style")
    data object NavToolbarShowTitle : PreferenceKey("nav_toolbar_show_title")
    data object NavBackButtonStyle : PreferenceKey("nav_back_button_style")
    data object CommentThreadNavToolbarStyle : PreferenceKey("comment_thread_nav_toolbar_style")
    data object CommentThreadNavToolbarShowTitle : PreferenceKey("comment_thread_nav_toolbar_show_title")
    data object CommentThreadNavBackButtonStyle : PreferenceKey("comment_thread_nav_back_button_style")
    data object ConvSpacingStyle : PreferenceKey("conversation_spacing_style")
    data object ConvSpacingBetweenComments : PreferenceKey("conversation_spacing_between_comments")
    data object ConvSpacingGuidelines : PreferenceKey("conversation_spacing_guidelines")
    data object ConvSpacingQuestions : PreferenceKey("conversation_spacing_questions")
    data object ConversationSettings : PreferenceKey("conversationSettings")

    // Internal - Environment
    data object Environment : PreferenceKey("environment")
    data object EnvironmentBaseUrl : PreferenceKey("environment_base_url")
    data object MockSsoEnvironment : PreferenceKey("mock_sso_environment")
    data object ApplyBaseUrl : PreferenceKey("apply_base_url")

    // Internal - Other
    data object LoggerView : PreferenceKey("logger_view")
    data object EndpointOverrides : PreferenceKey("endpoint_overrides")
    data object ClearConfig : PreferenceKey("clear_config")
    data object EnableLoginDelegation : PreferenceKey("enable_login_delegation")
    data object ProfileUrlPath : PreferenceKey("profile_url_path")

    // Authentication
    data object ShowLoginPrompt : PreferenceKey("show_login_prompt")
    data object RenewSSOMode : PreferenceKey("renew_sso_mode")
    data object ExpireAuthToken : PreferenceKey("expire_auth_token")
    data object ExpireAuthAndRefreshTokens : PreferenceKey("expire_auth_and_refresh_tokens")

    // Other Settings
    data object CommentCreationStyle : PreferenceKey("comment_creation_style")
    data object CommentThreadId : PreferenceKey("comment_thread_id")
    data object HideArticleHeader : PreferenceKey("hide_article_header")
    data object EnableSocialReviews : PreferenceKey("enable_social_reviews")
    data object EnablePullToRefresh : PreferenceKey("enable_pull_to_refresh")
    data object EnableReactions : PreferenceKey("enable_reactions")
    data object ReactionsThemeName : PreferenceKey("reactions_theme_name")
}
