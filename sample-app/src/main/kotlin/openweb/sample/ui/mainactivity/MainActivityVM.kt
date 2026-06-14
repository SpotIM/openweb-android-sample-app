package openweb.sample.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import spotIm.common.api.OWManager

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
) : MainActivityVMContract,
    MainActivityVMInputs,
    MainActivityVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _navigationEvent = MutableSharedFlow<NavigationDestination>(replay = 0)
    override val navigationEvent: SharedFlow<NavigationDestination> = _navigationEvent.asSharedFlow()

    override fun initializeArguments(destination: String?) {
        viewModelScope.launch {
            val navigationDestination =
                if (destination == MainActivityConstants.Destination.Settings) NavigationDestination.Settings
                else NavigationDestination.Home

            _navigationEvent.emit(navigationDestination)
        }
    }

}
