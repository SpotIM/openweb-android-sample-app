package openweb.sample.ui.screens.home

import androidx.compose.ui.graphics.Color

/**
 * Represents a content vertical category displayed on the home screen.
 *
 * Each card represents a different content type (News, Sports, Finance, etc.)
 * that demonstrates SDK integration patterns for that vertical.
 *
 * @property id Navigation route identifier for this vertical
 * @property icon Emoji representing the vertical category
 * @property title Display name of the vertical
 * @property description Brief explanation of what this vertical demonstrates
 * @property color Brand color for the vertical's UI theme
 */
data class VerticalCard(
    val id: String,
    val icon: String,
    val title: String,
    val description: String = "",
    val color: Color
)
