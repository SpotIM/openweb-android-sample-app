package openweb.sample.ui.screens.verticals.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import openweb.sample.data.local.OWScreenStyleProvider
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.GoalEvent
import openweb.sample.ui.screens.verticals.model.SportVerticalUiState
import openweb.sample.ui.screens.verticals.model.MockTeams
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.utils.initialization.SpotImInitializer

private data class MatchTick(
    val minute: Int,
    val homeGoal: Boolean = false,
    val awayGoal: Boolean = false,
    val fullTime: Boolean = false
)

interface SportVerticalScreenVMInputs {
    fun onImplementationInfoClicked()
    fun initializeBeforeFragment()
    fun onScreenResumed()
    fun buildConversationParams(): ConversationParams
}

interface SportVerticalScreenVMOutputs {
    val uiState: StateFlow<SportVerticalUiState>
    val mockData: VerticalMockData
    val settingsVersion: StateFlow<Int>
}

interface SportVerticalScreenContract {
    val inputs: SportVerticalScreenVMInputs
    val outputs: SportVerticalScreenVMOutputs
}

class SportVerticalScreenVM(
    override val mockData: VerticalMockData,
    private val screenStyleProvider: OWScreenStyleProvider,
    private val spotImInitializer: SpotImInitializer
) : SportVerticalScreenContract,
    SportVerticalScreenVMInputs,
    SportVerticalScreenVMOutputs,
    ViewModel() {

    override val inputs = this
    override val outputs = this

    private val _uiState = MutableStateFlow(SportVerticalUiState())
    override val uiState = _uiState.asStateFlow()

    private val _settingsVersion = MutableStateFlow(0)
    override val settingsVersion = _settingsVersion.asStateFlow()

    private var goalId = 0

    init {
        _uiState.update { it.copy(homeScore = 2, awayScore = 1, matchMinute = 65, isLive = true) }
        viewModelScope.launch {
            matchFlow().collect { event ->
                _uiState.update { state ->
                    var newState = state.copy(matchMinute = event.minute)
                    if (event.homeGoal) {
                        goalId++
                        newState = newState.copy(
                            homeScore = state.homeScore + 1,
                            goalEvent = GoalEvent(teamName = MockTeams.HOME_TEAM_NAME, id = goalId)
                        )
                    }
                    if (event.awayGoal) {
                        goalId++
                        newState = newState.copy(
                            awayScore = state.awayScore + 1,
                            goalEvent = GoalEvent(teamName = MockTeams.AWAY_TEAM_NAME, id = goalId)
                        )
                    }
                    if (event.fullTime) {
                        newState = newState.copy(isLive = false)
                    }
                    newState
                }
                if (event.homeGoal || event.awayGoal) {
                    viewModelScope.launch {
                        delay(3000)
                        _uiState.update { it.copy(goalEvent = null) }
                    }
                }
            }
        }
    }

    override fun onImplementationInfoClicked() =
        _uiState.update { it.copy(implementationInfoExpanded = !_uiState.value.implementationInfoExpanded) }

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

    private fun matchFlow(): Flow<MatchTick> = flow {
        for (minute in 66..90) {
            val goalRoll = Random.nextDouble()
            val homeGoal = goalRoll < 0.12
            val awayGoal = !homeGoal && goalRoll < 0.24
            emit(
                MatchTick(
                    minute = minute,
                    homeGoal = homeGoal,
                    awayGoal = awayGoal,
                    fullTime = minute == 90
                )
            )
        }
    }.onEach { delay(4000) }
}
