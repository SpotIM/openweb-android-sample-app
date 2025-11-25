package openweb.sample.ui.screens.verticals.components.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import openweb.sample.R
import openweb.sample.ui.screens.verticals.theme.videoColor

@Composable
fun VideoActionButtons(
    modifier: Modifier = Modifier,
    commentCountLabel: String,
    onCommentClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        EnhancedActionButton(
            iconRes = R.drawable.ic_heart,
            label = "24.5K"
        )

        EnhancedActionButton(
            iconRes = R.drawable.ic_comment,
            onClick = onCommentClick,
            label = commentCountLabel,
            tint = videoColor
        )

        EnhancedActionButton(
            iconRes = R.drawable.ic_share,
            label = "Share"
        )

        EnhancedActionButton(
            iconRes = R.drawable.ic_info,
            onClick = onInfoClick,
            label = "Info",
            tint = videoColor
        )
    }
}
