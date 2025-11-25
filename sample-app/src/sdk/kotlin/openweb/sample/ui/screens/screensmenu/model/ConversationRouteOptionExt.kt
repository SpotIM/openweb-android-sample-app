@file:JvmName("ConversationRouteOptionExtSdk")

package openweb.sample.ui.screens.screensmenu.model

fun ConversationRouteOption.requiresId() =
    this == ConversationRouteOption.COMMENT_CREATION_EDIT ||
        this == ConversationRouteOption.COMMENT_CREATION_REPLY ||
        this == ConversationRouteOption.COMMENT_THREAD

fun ConversationRouteOption.getInputHint(): String {
    return when (this) {
        ConversationRouteOption.COMMENT_CREATION_EDIT,
        ConversationRouteOption.COMMENT_CREATION_REPLY,
        ConversationRouteOption.COMMENT_THREAD -> "Comment ID"
        else -> ""
    }
}
