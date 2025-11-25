package openweb.sample.ui.examples.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * No-op stub returning a fixed keyboard height of 0dp for public builds.
 *
 * The internal variant provides dynamic keyboard height detection for advanced UI adjustments.
 * This stub ensures consistent behavior in the public sample app.
 *
 * @return State containing 0dp keyboard height
 */
@Composable
fun rememberKeyboardHeight(): State<Dp> {
    return remember { mutableStateOf(0.dp) }
}
