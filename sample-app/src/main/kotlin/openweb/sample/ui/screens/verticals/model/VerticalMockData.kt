package openweb.sample.ui.screens.verticals.model

import androidx.compose.ui.graphics.Color
import openweb.sample.data.local.OWArticleSettingsProvider
import openweb.sample.ui.screens.verticals.components.article.ImplementationInfo
import openweb.sample.ui.screens.verticals.theme.financeColor
import openweb.sample.ui.screens.verticals.theme.newsColor
import openweb.sample.ui.screens.verticals.theme.recipesColor
import openweb.sample.ui.screens.verticals.theme.sideRailColor
import openweb.sample.ui.screens.verticals.theme.sportsColor
import openweb.sample.ui.screens.verticals.theme.videoColor
import spotIm.common.api.model.settings.article.OWArticleSettings

/**
 * Specifies which SDK fragment type to display for a vertical.
 */
enum class FragmentType {
    /** Full conversation screen */
    Conversation,

    /** Pre-conversation preview screen */
    PreConversation
}

/**
 * Mock data container for different content verticals in the sample app.
 *
 * Each vertical (News, Sports, Finance, etc.) provides mock article data,
 * styling, and SDK configuration to demonstrate different use cases and
 * integration patterns.
 *
 * @property color Theme color for the vertical
 * @property article Mock article content and metadata
 * @property implementationInfo Technical implementation details for developers
 * @property articleSettings SDK article configuration
 * @property fragmentType Which SDK fragment type to display
 */
sealed class VerticalMockData(
    val title: String = "News",
    val color: Color,
    val article: ArticleData,
    val implementationInfo: ImplementationInfo,
    open val articleSettings: OWArticleSettings = OWArticleSettings(),
    val fragmentType: FragmentType = FragmentType.PreConversation
) {
    data class News(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "News",
        color = newsColor,
        article = MockArticles.news(),
        implementationInfo = MockImplementationInfo.news(),
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.news(provider)
    }

    data class Finance(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "Finance",
        color = financeColor,
        article = MockArticles.finance(),
        implementationInfo = MockImplementationInfo.finance(),
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.finance(provider)
    }

    data class Recipes(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "Recipes",
        color = recipesColor,
        article = MockArticles.recipes(),
        implementationInfo = MockImplementationInfo.recipes(),
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.recipes(provider)
    }

    data class Sport(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "Sport",
        color = sportsColor,
        article = MockArticles.sport(),
        implementationInfo = MockImplementationInfo.sport(),
        fragmentType = FragmentType.Conversation
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.sport(provider)
    }

    data class Video(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "Video",
        color = videoColor,
        article = MockArticles.video(),
        implementationInfo = MockImplementationInfo.video(),
        fragmentType = FragmentType.Conversation
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.video(provider)
    }

    data class SideRail(val provider: OWArticleSettingsProvider) : VerticalMockData(
        title = "Side Rail",
        color = sideRailColor,
        article = MockArticles.sideRail(),
        implementationInfo = MockImplementationInfo.sideRail(),
        fragmentType = FragmentType.Conversation
    ) {
        override val articleSettings: OWArticleSettings
            get() = MockArticleSettings.sideRail(provider)
    }
}
