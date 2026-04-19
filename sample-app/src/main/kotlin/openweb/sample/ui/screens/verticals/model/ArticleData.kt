package openweb.sample.ui.screens.verticals.model

import androidx.annotation.DrawableRes

data class ArticleData(
    val conversationIds: ConversationIdentifiers,
    val title: String,
    val paragraphs: List<String>,
    @DrawableRes val imageRes: Int? = null,
    val source: String? = null,
    val readTimeMinutes: Int? = null,
    val subheader: String? = null,
    val authorName: String? = null,
    val authorDate: String? = null
)
