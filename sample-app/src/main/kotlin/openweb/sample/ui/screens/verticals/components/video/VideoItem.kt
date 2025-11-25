package openweb.sample.ui.screens.verticals.components.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi

@UnstableApi
@Composable
internal fun VideoItem(
    videoUrl: String,
    isVisible: Boolean,
    onBackClick: () -> Unit,
    commentCountLabel: String,
    onCommentClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundVideoPlayer(
            modifier = Modifier.fillMaxSize(),
            videoUrl = videoUrl,
            shouldPlay = isVisible
        )

        VideoTopBar(
            modifier = Modifier.align(Alignment.TopCenter),
            onBackClick = onBackClick
        )

        VideoActionButtons(
            modifier = Modifier.align(Alignment.CenterEnd),
            commentCountLabel = commentCountLabel,
            onCommentClick = onCommentClick,
            onInfoClick = onInfoClick
        )

        VideoBottomContent(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 92.dp, bottom = 36.dp)
        )
    }
}
