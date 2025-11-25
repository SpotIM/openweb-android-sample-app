package openweb.sample.ui.screens.screensmenu.model

import spotIm.common.api.model.OWCommentCreationType
import spotIm.common.api.model.settings.OWConversationRoute

enum class ConversationRouteOption(val displayName: String) {
    NONE("None"),
    COMMENT_CREATION_ADD("Add Comment"),
    COMMENT_CREATION_EDIT("Edit Comment"),
    COMMENT_CREATION_REPLY("Reply Comment"),
    COMMENT_THREAD("Comment Thread");

    fun toOWConversationRoute(extraRouteId: String? = null): OWConversationRoute? {
        return when (this) {
            NONE -> null
            COMMENT_CREATION_ADD -> OWConversationRoute.OWCommentCreationRoute(
                type = OWCommentCreationType.Comment
            )
            COMMENT_CREATION_EDIT -> OWConversationRoute.OWCommentCreationRoute(
                type = OWCommentCreationType.Edit(
                    commentId = extraRouteId.orEmpty()
                )
            )
            COMMENT_CREATION_REPLY -> OWConversationRoute.OWCommentCreationRoute(
                type = OWCommentCreationType.ReplyTo(
                    commentId = extraRouteId.orEmpty()
                )
            )
            COMMENT_THREAD -> OWConversationRoute.OWCommentThreadRoute(
                commentId = extraRouteId.orEmpty()
            )
        }
    }
}
