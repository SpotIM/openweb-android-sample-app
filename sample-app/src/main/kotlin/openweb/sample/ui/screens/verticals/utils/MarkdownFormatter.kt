package openweb.sample.ui.screens.verticals.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

object MarkdownFormatter {
    /**
     * Converts markdown-style text with **bold** syntax to AnnotatedString with proper styling.
     */
    private val boldPattern = Regex("""\*\*(.+?)\*\*""")

    fun parseMarkdown(text: String): AnnotatedString {
        return buildAnnotatedString {
            var currentIndex = 0

            boldPattern.findAll(text).forEach { matchResult ->
                // Append text before the bold section
                if (currentIndex < matchResult.range.first) {
                    append(text.substring(currentIndex, matchResult.range.first))
                }

                // Append bold text
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(matchResult.groupValues[1])
                }

                currentIndex = matchResult.range.last + 1
            }

            // Append remaining text after the last bold section
            if (currentIndex < text.length) {
                append(text.substring(currentIndex))
            }
        }
    }
}
