package openweb.sample.ui.screens.verticals.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import openweb.sample.data.local.OWScreenStyleProvider
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.SiderailVerticalUiState
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.utils.initialization.SpotImInitializer

interface SiderailVerticalScreenVMInputs {
    fun onImplementationInfoClicked()
    fun onDrawerToggle()
    fun initializeBeforeFragment()
    fun onScreenResumed()
    fun buildConversationParams(): ConversationParams
}

interface SiderailVerticalScreenVMOutputs {
    val uiState: StateFlow<SiderailVerticalUiState>
    val mockData: VerticalMockData
    val settingsVersion: StateFlow<Int>
}

interface SiderailVerticalScreenContract {
    val inputs: SiderailVerticalScreenVMInputs
    val outputs: SiderailVerticalScreenVMOutputs
}

class SiderailVerticalScreenVM(
    override val mockData: VerticalMockData,
    private val screenStyleProvider: OWScreenStyleProvider,
    private val spotImInitializer: SpotImInitializer
) : SiderailVerticalScreenContract,
    SiderailVerticalScreenVMInputs,
    SiderailVerticalScreenVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _uiState = MutableStateFlow(SiderailVerticalUiState())
    override val uiState = _uiState.asStateFlow()

    private val _settingsVersion = MutableStateFlow(0)
    override val settingsVersion = _settingsVersion.asStateFlow()

    override fun onImplementationInfoClicked() =
        _uiState.update { it.copy(implementationInfoExpanded = !_uiState.value.implementationInfoExpanded) }

    override fun onDrawerToggle() = _uiState.update { it.copy(isDrawerOpen = !_uiState.value.isDrawerOpen) }

    override fun initializeBeforeFragment() {
        spotImInitializer.init(mockData.article.conversationIds.spotId)
    }

    override fun onScreenResumed() {
        initializeBeforeFragment()
        _settingsVersion.update { it + 1 }
    }

    override fun buildConversationParams(): ConversationParams =
        ConversationParams(
            postId = mockData.article.conversationIds.postId,
            articleSettings = mockData.articleSettings,
            additionalSettings = screenStyleProvider.provideAdditionalSettings(),
            brandColor = mockData.color
        )
}
