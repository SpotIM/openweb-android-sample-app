package openweb.sample.data.local

import spotIm.common.api.model.Article
import spotIm.common.api.model.settings.article.OWArticleAdditionalSettings
import spotIm.common.api.model.settings.article.OWArticleHeaderStyle
import spotIm.common.api.model.settings.article.OWArticleInformationStrategy
import spotIm.common.api.model.settings.article.OWArticleSettings
import spotIm.common.api.model.settings.article.OWReadOnlyMode
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy
import openweb.sample.utils.mock

/**
 * Provides [OWArticleSettings] configuration based on user preferences.
 *
 * Translates sample app settings into SDK article configuration including:
 * - Article information strategy (server vs local)
 * - Header visibility
 * - Read-only mode
 * - Star ratings/social reviews
 *
 * @param settingsRepository Source of user preferences
 */
class OWArticleSettingsProvider(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Builds article settings from current app configuration.
     *
     * @return Configured article settings for SDK initialization
     */
    fun provide(): OWArticleSettings {
        val articleSection = settingsRepository[SharedPrefsItem.ArticleSection]
        val localArticle = getLocalArticle()
        val readOnlyMode = getReadOnlyMode()
        val starRating = getStarRating()

        val shouldHideArticleHeader = settingsRepository[SharedPrefsItem.ArticleHeaderStyle]
        val articleHeaderStyle = if (shouldHideArticleHeader) {
            OWArticleHeaderStyle.None
        } else {
            OWArticleHeaderStyle.Regular
        }

        val articleInformationStrategy = when (settingsRepository[SharedPrefsItem.ArticleInformationStrategy]) {
            ArticleInformationStrategy.Server -> OWArticleInformationStrategy.Server
            ArticleInformationStrategy.Local -> {
                OWArticleInformationStrategy.Local(article = localArticle)
            }
        }

        return OWArticleSettings(
            articleInformationStrategy = articleInformationStrategy,
            additionalSettings = OWArticleAdditionalSettings(
                section = articleSection,
                headerStyle = articleHeaderStyle,
                readOnlyMode = readOnlyMode,
                starRating = starRating
            )
        )
    }

    private fun getLocalArticle(): Article {
        val url = settingsRepository[SharedPrefsItem.ArticleAssociatedUrl]
        return if (url.isNullOrEmpty()) Article.mock() else Article.mock().copy(url = url)
    }

    private fun getReadOnlyMode(): OWReadOnlyMode = settingsRepository[SharedPrefsItem.ReadOnlyMode]

    private fun getStarRating(): Boolean = settingsRepository[SharedPrefsItem.EnableSocialReviews]
}
