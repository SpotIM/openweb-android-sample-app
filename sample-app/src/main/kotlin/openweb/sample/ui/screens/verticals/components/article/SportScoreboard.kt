package openweb.sample.ui.screens.verticals.components.article

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import openweb.sample.ui.screens.verticals.model.GoalEvent
import openweb.sample.ui.screens.verticals.model.MockTeams
import openweb.sample.ui.screens.verticals.theme.sportsColor

@Composable
fun SportScoreboard(
    modifier: Modifier = Modifier,
    homeTeamName: String = MockTeams.HOME_TEAM_NAME,
    homeTeamLogo: Painter? = painterResource(MockTeams.HOME_LOGO_RES),
    homeScore: Int = 2,
    awayTeamName: String = MockTeams.AWAY_TEAM_NAME,
    awayTeamLogo: Painter? = painterResource(MockTeams.AWAY_LOGO_RES),
    awayScore: Int = 1,
    matchMinute: Int = 0,
    isLive: Boolean = true,
    goalEvent: GoalEvent? = null,
    brandColor: Color = sportsColor
) {
    // Animatables for scale bounce on goal
    val homeScoreScale = remember { Animatable(1f) }
    val awayScoreScale = remember { Animatable(1f) }

    // Animate color: yellow while goal is active for that team, white otherwise
    val homeScoreColor by animateColorAsState(
        targetValue = if (goalEvent != null && goalEvent.teamName == homeTeamName) Color.Yellow else Color.White,
        animationSpec = tween(300),
        label = "homeScoreColor"
    )
    val awayScoreColor by animateColorAsState(
        targetValue = if (goalEvent != null && goalEvent.teamName == awayTeamName) Color.Yellow else Color.White,
        animationSpec = tween(300),
        label = "awayScoreColor"
    )

    // Trigger scale pulse when a new goal fires
    LaunchedEffect(goalEvent?.id) {
        if (goalEvent == null) return@LaunchedEffect
        val scaleAnim = if (goalEvent.teamName == homeTeamName) homeScoreScale else awayScoreScale
        scaleAnim.snapTo(1f)
        scaleAnim.animateTo(1.5f, animationSpec = tween(200))
        scaleAnim.animateTo(1f, animationSpec = tween(200))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brandColor)
            .animateContentSize()
    ) {
        // Status bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLive) {
                val infiniteTransition = rememberInfiniteTransition(label = "livePulse")
                val dotAlpha by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0.3f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 800),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dotAlpha"
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Red LIVE pill chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFFF3B30))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(modifier = Modifier.size(6.dp)) {
                                drawCircle(color = Color.White.copy(alpha = dotAlpha))
                            }
                            Text(
                                text = "LIVE",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "· ${matchMinute}'",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            } else {
                Text(
                    text = "FULL TIME",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.5.sp
                )
            }
        }

        // Teams & scores row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home team
            TeamWithScore(
                name = homeTeamName,
                logo = homeTeamLogo,
                modifier = Modifier.weight(1f)
            )

            // Score
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = homeScore.toString(),
                    color = homeScoreColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    modifier = Modifier.graphicsLayer {
                        scaleX = homeScoreScale.value
                        scaleY = homeScoreScale.value
                    }
                )
                Text(
                    text = " - ",
                    color = Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp
                )
                Text(
                    text = awayScore.toString(),
                    color = awayScoreColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    modifier = Modifier.graphicsLayer {
                        scaleX = awayScoreScale.value
                        scaleY = awayScoreScale.value
                    }
                )
            }

            // Away team
            TeamWithScore(
                name = awayTeamName,
                logo = awayTeamLogo,
                modifier = Modifier.weight(1f)
            )
        }
        // Embedded GOAL! banner — inside the red card, below scores
        AnimatedVisibility(
            visible = goalEvent != null,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "\u26BD", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GOAL!",
                    color = Color.Yellow,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
private fun TeamWithScore(
    name: String,
    logo: Painter?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.15f),
                        radius = size.minDimension / 2f + 10.dp.toPx()
                    )
                }
                .shadow(elevation = 6.dp, shape = CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (logo != null) {
                Image(
                    painter = logo,
                    contentDescription = "$name logo",
                    modifier = Modifier.size(52.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Canvas(modifier = Modifier.matchParentSize()) {
                drawOval(
                    color = Color.White.copy(alpha = 0.12f),
                    topLeft = Offset(
                        x = size.width * 0.15f,
                        y = size.height * 0.05f
                    ),
                    size = Size(
                        width = size.width * 0.7f,
                        height = size.height * 0.4f
                    )
                )
            }
        }

        Text(
            text = name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    offset = Offset(1f, 1f),
                    blurRadius = 3f
                )
            )
        )
    }
}
