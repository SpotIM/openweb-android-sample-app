package openweb.sample.ui.screens.verticals.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import openweb.sample.data.local.OWScreenStyleProvider
import openweb.sample.ui.screens.verticals.model.ArticleVerticalUiState
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.utils.initialization.SpotImInitializer

interface ArticleVerticalScreenVMInputs {
    fun onImplementationInfoClicked()
    fun initializeBeforeFragment()
    fun onScreenResumed()
    fun buildConversationParams(): ConversationParams
}

interface ArticleVerticalScreenVMOutputs {
    val uiState: StateFlow<ArticleVerticalUiState>
    val mockData: VerticalMockData
    val settingsVersion: StateFlow<Int>
}

interface ArticleVerticalScreenContract {
    val inputs: ArticleVerticalScreenVMInputs
    val outputs: ArticleVerticalScreenVMOutputs
}

class ArticleVerticalScreenVM(
    override val mockData: VerticalMockData,
    private val screenStyleProvider: OWScreenStyleProvider,
    private val spotImInitializer: SpotImInitializer
) : ArticleVerticalScreenContract,
    ArticleVerticalScreenVMInputs,
    ArticleVerticalScreenVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _uiState = MutableStateFlow(ArticleVerticalUiState())
    override val uiState = _uiState.asStateFlow()

    // Version counter that increments whenever we return to the screen
    // This ensures the fragment is recreated with fresh settings
    private val _settingsVersion = MutableStateFlow(0)
    override val settingsVersion = _settingsVersion.asStateFlow()

    override fun onImplementationInfoClicked() {
        _uiState.update { it.copy(implementationInfoExpanded = !_uiState.value.implementationInfoExpanded) }
    }

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
