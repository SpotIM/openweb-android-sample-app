package openweb.sample.ui.model

import androidx.annotation.Keep

/**
 * Defines the different OpenWeb SDK UI components that can be displayed.
 *
 * Used to specify which SDK screen or component to show when launching examples.
 * Supports both fragment-based and intent-based display modes.
 */
@Keep
enum class OWComponentToDisplay {
    /** Pre-conversation fragment (comments preview before full conversation) */
    PRE_CONVERSATION_FRAGMENT,

    /** Pre-conversation fragment embedded in a RecyclerView */
    PRE_CONVERSATION_FRAGMENT_IN_RECYCLER_VIEW,

    /** Pre-conversation screen using route-based navigation */
    PRE_CONVERSATION_ROUTE,

    /** Full conversation screen launched via Intent */
    CONVERSATION_INTENT,

    /** Full conversation screen as a Fragment */
    CONVERSATION_FRAGMENT,

    /** Full conversation screen using route-based navigation */
    CONVERSATION_ROUTE,

    /** Comment creation screen launched via Intent */
    CREATE_COMMENT_INTENT,

    /** Comment creation screen as a Fragment */
    CREATE_COMMENT_FRAGMENT,

    /** Comment thread screen launched via Intent */
    COMMENT_THREAD_INTENT,

    /** Comment thread screen as a Fragment */
    COMMENT_THREAD_FRAGMENT,

    /** Report reasons selection screen */
    REPORT_REASONS_FRAGMENT
}
