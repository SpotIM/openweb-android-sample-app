package openweb.sample.ui.screens.verticals.components.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import openweb.sample.R

@Composable
fun VideoBottomContent(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CreatorInfoRow(
            modifier = Modifier.padding(bottom = 10.dp)
        )

        VideoDescription(
            modifier = Modifier.padding(bottom = 14.dp)
        )

        MusicInfo()
    }
}

@Composable
private fun CreatorInfoRow(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Text(
            text = "@creator_official",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = androidx.compose.ui.geometry.Offset(0f, 1f),
                    blurRadius = 3f
                )
            )
        )
        FollowButton()
    }
}

@Composable
private fun FollowButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(2.dp, CircleShape)
            .background(Color.White.copy(alpha = 0.25f), CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "Follow",
            style = TextStyle(
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f),
                    offset = androidx.compose.ui.geometry.Offset(0f, 1f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
private fun VideoDescription(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Behind the Scenes: Making of a Viral Hit ✨ #viral #trending #openweb",
        style = TextStyle(
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.6f),
                offset = androidx.compose.ui.geometry.Offset(0f, 1f),
                blurRadius = 3f
            )
        ),
        modifier = modifier
    )
}

@Composable
private fun MusicInfo(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_music),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(16.dp)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint()
                        paint.color = Color.Black.copy(alpha = 0.4f).toArgb()
                        paint.maskFilter = BlurMaskFilter(
                            4f,
                            BlurMaskFilter.Blur.NORMAL
                        )
                        canvas.nativeCanvas.drawCircle(
                            size.width / 2,
                            size.height / 2,
                            size.width / 2,
                            paint
                        )
                    }
                }
        )
        Text(
            text = "Original Sound - Creator Official 🎵",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = androidx.compose.ui.geometry.Offset(0f, 1f),
                    blurRadius = 3f
                )
            ),
            maxLines = 1
        )
    }
}
