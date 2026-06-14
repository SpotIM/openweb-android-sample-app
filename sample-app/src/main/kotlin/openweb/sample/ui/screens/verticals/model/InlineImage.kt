package openweb.sample.ui.screens.verticals.model

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

data class InlineImage(
    @DrawableRes val imageRes: Int,
    @IntRange(from = 0) val afterParagraphIndex: Int,
) {
    init {
        require(afterParagraphIndex >= 0) {
            "afterParagraphIndex must be >= 0, got $afterParagraphIndex"
        }
    }
}
