package openweb.sample.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import spotIm.common.api.OWManager
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.features.customui.SampleAppCustomUIDelegate
import openweb.sample.utils.GiphyHandler

/**
 * ViewModel for MainActivity.
 * Handles activity-level concerns like custom UI setup and analytics.
 */
sealed class NavigationDestination {
    object Home : NavigationDestination()
    object Settings : NavigationDestination()
}

interface MainActivityVMInputs {
    fun initializeArguments(destination: String?)
}

interface MainActivityVMOutputs {
    val navigationEvent: SharedFlow<NavigationDestination>
}

interface MainActivityVMContract {
    val inputs: MainActivityVMInputs
    val outputs: MainActivityVMOutputs
}

class MainActivityVM(
    private val owManager: OWManager,
    private val settingsRepository: SettingsRepository
) : MainActivityVMContract,
    MainActivityVMInputs,
    MainActivityVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _navigationEvent = MutableSharedFlow<NavigationDestination>(replay = 0)
    override val navigationEvent: SharedFlow<NavigationDestination> = _navigationEvent.asSharedFlow()

    init {
        setupCustomUIDelegates()
        setupGiphyProvider()
    }

    override fun initializeArguments(destination: String?) {
        viewModelScope.launch {
            val navigationDestination =
                if (destination == MainActivityConstants.Destination.Settings) NavigationDestination.Settings
                else NavigationDestination.Home

            _navigationEvent.emit(navigationDestination)
        }
    }

    private fun setupCustomUIDelegates() =
        owManager.ui.customizations.setCustomUIDelegate(SampleAppCustomUIDelegate(settingsRepository))

    private fun setupGiphyProvider() =
        owManager.ui.customizations.setGiphyProvider(GiphyHandler)
}
