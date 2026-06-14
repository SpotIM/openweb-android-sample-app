package openweb.sample.ui.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import openweb.sample.R
import openweb.sample.databinding.ActivityMainContainerBinding
import openweb.sample.ui.navigation.FragmentNavigator
import openweb.sample.utils.handleNotificationIntent
import openweb.sample.utils.supportEdgeToEdge
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Container activity for fragment-based navigation in the sample app.
 *
 * Used primarily in the internal build variant for testing various SDK screens and configurations.
 * The public variant uses [VerticalHomeActivity] as its main entry point, with this activity
 * reserved for Settings screens.
 *
 * Handles:
 * - Fragment navigation via [FragmentNavigator]
 * - Toolbar menu actions (Settings, Authentication)
 * - Back navigation with proper fragment stack management
 */
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainContainerBinding? = null
    private val binding: ActivityMainContainerBinding get() = _binding!!

    private val viewModel: MainActivityVMContract by viewModel<MainActivityVM>()
    private val navigator: FragmentNavigator by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            if (!navigator.popBackStack()) finish()
        }
        supportFragmentManager.addOnBackStackChangedListener { syncBackArrow() }
        syncBackArrow()

        observeNavigationEvents()

        if (savedInstanceState == null) {
            val destination = intent.getStringExtra(MainActivityConstants.ExtraNavigateTo)
            viewModel.inputs.initializeArguments(destination)
        }

        setupBackPressHandling()
        handleNotificationIntent(intent)
    }

    private fun syncBackArrow() {
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            viewModel.outputs.navigationEvent.collect { destination ->
                when (destination) {
                    NavigationDestination.Home -> navigator.navigateToHome()
                    NavigationDestination.Settings -> navigator.navigateToSettings()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_default, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.auth -> {
                navigator.navigateToAuthentication()
                return true
            }

            R.id.settings -> {
                navigator.navigateToSettings()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupBackPressHandling() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() =
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    } else {
                        finish()
                    }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
