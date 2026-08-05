package openweb.sample.ui.screens.verticals.components.article

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import openweb.sample.R
import openweb.sample.ui.screens.verticals.theme.componentBorderColor
import spotIm.compose.Reactions

/**
 * Hosts the public [Reactions] widget inside a collapsible card that mirrors [ImplementationInfoCard].
 *
 * Only the header row toggles the card — a tap on the widget itself is the vote gesture and must not
 * be intercepted. The widget is composed only while [expanded]; collapsing removes it from the
 * composition. Re-expanding (or the item being recycled back on-screen) recreates the underlying
 * view, but the SDK serves its reactions from an in-memory cache, so no network reload occurs. The
 * last measured height is reserved via [rememberSaveable] so the recreated widget lays out at full
 * height on the first pass instead of growing from zero and shifting the list.
 */
@Composable
fun ReactionsCard(
    postId: String,
    themeName: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(1.dp, componentBorderColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_home_reactions),
                        contentDescription = "Reactions",
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Reactions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tap an option to cast your vote",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    // Reserve the widget's last measured height so a recycled LazyColumn item
                    // re-composes at full height on the first layout pass instead of growing from
                    // zero as the widget's content lays out — which otherwise shifts the list (jump).
                    // rememberSaveable survives the item's disposal while it is scrolled off-screen.
                    val density = LocalDensity.current
                    var lastHeightPx by rememberSaveable { mutableStateOf(0) }
                    Reactions(
                        postId = postId,
                        themeName = themeName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = with(density) { lastHeightPx.toDp() })
                            .onSizeChanged { size -> if (size.height > 0) lastHeightPx = size.height }
                    )
                }
            }
        }
    }
}
