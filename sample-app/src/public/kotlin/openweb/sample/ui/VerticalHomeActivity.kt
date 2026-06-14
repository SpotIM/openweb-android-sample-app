package openweb.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import openweb.sample.ui.mainactivity.MainActivity
import openweb.sample.ui.mainactivity.MainActivityConstants
import openweb.sample.ui.screens.examples.compose.ui.theme.ComposeTheme
import openweb.sample.ui.screens.home.VerticalNavHost
import openweb.sample.utils.handleNotificationIntent

/**
 * Main entry point activity for the public sample app, showcasing the OpenWeb SDK.
 *
 * This activity uses Jetpack Compose to display a home screen with various content verticals
 * (News, Sports, Finance, etc.). Each vertical demonstrates different SDK integration patterns.
 * Settings screens use fragments and are accessed via [MainActivity].
 *
 * Extends [FragmentActivity] to support fragment-based SDK components embedded in Compose screens.
 */
class VerticalHomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleNotificationIntent(intent)

        setContent {
            ComposeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            WindowInsets.ime
                                .union(WindowInsets.systemBars)
                                .asPaddingValues()
                        )
                        .consumeWindowInsets(WindowInsets.systemBars),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    VerticalNavHost(
                        navController = navController,
                        onNavigateToSettings = {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra(
                                    MainActivityConstants.ExtraNavigateTo,
                                    MainActivityConstants.Destination.Settings
                                )
                            }
                            startActivity(intent)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }
}
