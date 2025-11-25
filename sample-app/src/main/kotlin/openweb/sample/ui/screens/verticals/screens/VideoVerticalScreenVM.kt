package openweb.sample.ui.screens.verticals.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import openweb.sample.data.local.OWScreenStyleProvider
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.ui.screens.verticals.model.VideoVerticalUiState
import openweb.sample.utils.initialization.SpotImInitializer
import openweb.sample.utils.logger.SampleLogger
import spotIm.common.api.OWManager
import spotIm.common.api.callbacks.SpotCallback
import spotIm.common.api.exceptions.SpotException
import spotIm.common.api.model.ConversationCounters
import spotIm.common.api.model.settings.conversation.OWConversationSettings
import spotIm.common.api.model.settings.conversation.style.OWConversationStyle

interface VideoVerticalScreenVMInputs {
    fun onCommentButtonClicked()
    fun onBottomSheetDismissed()
    fun onInfoButtonClicked()
    fun onInfoDialogDismissed()
    fun initializeBeforeFragment()
    fun onScreenResumed()
    fun buildConversationParams(): ConversationParams
}

interface VideoVerticalScreenVMOutputs {
    val uiState: StateFlow<VideoVerticalUiState>
    val mockData: VerticalMockData
    val settingsVersion: StateFlow<Int>
}

interface VideoVerticalScreenContract {
    val inputs: VideoVerticalScreenVMInputs
    val outputs: VideoVerticalScreenVMOutputs
}

class VideoVerticalScreenVM(
    override val mockData: VerticalMockData,
    private val screenStyleProvider: OWScreenStyleProvider,
    private val spotImInitializer: SpotImInitializer,
    private val owManager: OWManager
) : VideoVerticalScreenContract,
    VideoVerticalScreenVMInputs,
    VideoVerticalScreenVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _uiState = MutableStateFlow(VideoVerticalUiState())
    override val uiState = _uiState.asStateFlow()

    private val _settingsVersion = MutableStateFlow(0)
    override val settingsVersion = _settingsVersion.asStateFlow()

    override fun onCommentButtonClicked() {
        _uiState.update { it.copy(isBottomSheetVisible = true) }
    }

    override fun onBottomSheetDismissed() {
        _uiState.update { it.copy(isBottomSheetVisible = false) }
    }

    override fun onInfoButtonClicked() {
        _uiState.update { it.copy(isInfoDialogVisible = true) }
    }

    override fun onInfoDialogDismissed() {
        _uiState.update { it.copy(isInfoDialogVisible = false) }
    }

    override fun initializeBeforeFragment() {
        spotImInitializer.init(mockData.article.conversationIds.spotId)
        loadConversationCounters()
    }

    override fun onScreenResumed() {
        initializeBeforeFragment()
        _settingsVersion.update { it + 1 }
    }

    override fun buildConversationParams(): ConversationParams {
        return ConversationParams(
            postId = mockData.article.conversationIds.postId,
            articleSettings = mockData.articleSettings,
            additionalSettings = screenStyleProvider.provideAdditionalSettings().copy(
                conversationSettings = OWConversationSettings(conversationStyle = OWConversationStyle.Compact)
            ),
            brandColor = mockData.color
        )
    }

    private fun loadConversationCounters() {
        val postId = mockData.article.conversationIds.postId
        owManager.helpers.getConversationCounters(
            postIds = listOf(postId),
            callback = object : SpotCallback<Map<String, ConversationCounters>> {
                override fun onSuccess(response: Map<String, ConversationCounters>) {
                    val count = response[postId]?.comments
                    _uiState.update { it.copy(commentCount = count) }
                }

                override fun onFailure(exception: SpotException) {
                    SampleLogger.e("Failed to get conversation counters", e = exception)
                }
            }
        )
    }
}
