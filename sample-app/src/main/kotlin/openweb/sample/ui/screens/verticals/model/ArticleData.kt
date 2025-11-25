package openweb.sample.ui.screens.verticals.model

data class ArticleData(
    val conversationIds: ConversationIdentifiers,
    val title: String,
    val paragraphs: List<String>
)
