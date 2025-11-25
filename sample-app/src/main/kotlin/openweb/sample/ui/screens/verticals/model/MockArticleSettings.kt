package openweb.sample.ui.screens.verticals.model

import openweb.sample.data.local.OWArticleSettingsProvider
import spotIm.common.api.model.settings.article.OWArticleSettings

object MockArticleSettings {
    fun news(provider: OWArticleSettingsProvider) = provider.provide()

    fun finance(provider: OWArticleSettingsProvider): OWArticleSettings {
        val articleSettings = provider.provide()

        return articleSettings.copy(
            additionalSettings = articleSettings.additionalSettings.copy(
                section = "stock"
            )
        )
    }

    fun recipes(provider: OWArticleSettingsProvider): OWArticleSettings {
        val articleSettings = provider.provide()

        return articleSettings.copy(
            additionalSettings = articleSettings.additionalSettings.copy(
                starRating = true
            )
        )
    }

    fun sport(provider: OWArticleSettingsProvider) = provider.provide()

    fun video(provider: OWArticleSettingsProvider) = provider.provide()

    fun sideRail(provider: OWArticleSettingsProvider) = provider.provide()
}
