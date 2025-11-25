package openweb.sample.ui.screens.verticals.components.article

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import openweb.sample.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalTopAppBar(
    title: String,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    containerColor: Color = Color.Black,
    contentColor: Color = Color.White,
    showConversationButton: Boolean = false,
    onConversationClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            if (showConversationButton) {
                IconButton(onClick = onConversationClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment),
                        contentDescription = "See Conversation"
                    )
                }
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}
