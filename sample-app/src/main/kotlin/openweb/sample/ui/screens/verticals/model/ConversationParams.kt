package openweb.sample.ui.screens.verticals.model

import androidx.compose.ui.graphics.Color
import spotIm.common.api.helpers.OWPostId
import spotIm.common.api.model.settings.OWAdditionalSettings
import spotIm.common.api.model.settings.article.OWArticleSettings

data class ConversationParams(
    val postId: OWPostId,
    val articleSettings: OWArticleSettings,
    val additionalSettings: OWAdditionalSettings,
    val brandColor: Color
)
