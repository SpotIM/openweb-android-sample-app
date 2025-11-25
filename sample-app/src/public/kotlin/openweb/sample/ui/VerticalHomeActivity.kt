package openweb.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import openweb.sample.ui.screens.home.VerticalNavHost
import openweb.sample.ui.mainactivity.MainActivity
import openweb.sample.ui.mainactivity.MainActivityConstants
import openweb.sample.ui.screens.examples.compose.ui.theme.ComposeTheme

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

        setContent {
            ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    )
                }
            }
        }
    }
}
