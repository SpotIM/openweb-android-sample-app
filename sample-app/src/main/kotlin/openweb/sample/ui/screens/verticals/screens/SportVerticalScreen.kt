package openweb.sample.ui.screens.verticals.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import openweb.sample.ui.screens.verticals.components.article.ConversationContainer
import openweb.sample.ui.screens.verticals.components.article.ImplementationInfoCard
import openweb.sample.ui.screens.verticals.components.article.ReactionsCard
import openweb.sample.ui.screens.verticals.components.article.SportScoreboard
import openweb.sample.ui.screens.verticals.components.article.VerticalTopAppBar
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.ui.screens.verticals.theme.sportsColor
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportVerticalScreen(
    mockData: VerticalMockData,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val viewModel: SportVerticalScreenContract = koinViewModel<SportVerticalScreenVM> { parametersOf(mockData) }
    val uiState by viewModel.outputs.uiState.collectAsStateWithLifecycle()
    val settingsVersion by viewModel.outputs.settingsVersion.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    val activity = LocalContext.current as FragmentActivity
    val fragmentManager = activity.supportFragmentManager

    LaunchedEffect(Unit) {
        viewModel.inputs.initializeBeforeFragment()
    }

    DisposableEffect(Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.inputs.onScreenResumed()
            }
        }
        activity.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            VerticalTopAppBar(
                title = "Sport",
                containerColor = sportsColor,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            SportScoreboard(
                modifier = Modifier.zIndex(1f),
                homeScore = uiState.homeScore,
                awayScore = uiState.awayScore,
                matchMinute = uiState.matchMinute,
                isLive = uiState.isLive,
                goalEvent = uiState.goalEvent
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
            ) {
                item {
                    ImplementationInfoCard(
                        info = mockData.implementationInfo,
                        expanded = uiState.implementationInfoExpanded,
                        onToggle = { viewModel.inputs.onImplementationInfoClicked() },
                        iconColor = mockData.color
                    )
                }

                item {
                    ReactionsCard(
                        postId = mockData.article.conversationIds.postId,
                        themeName = mockData.title,
                        expanded = uiState.reactionsExpanded,
                        onToggle = { viewModel.inputs.onReactionsClicked() },
                        iconColor = mockData.color
                    )
                }

                item {
                    key(settingsVersion) {
                        ConversationContainer(
                            params = viewModel.inputs.buildConversationParams(),
                            fragmentManager = fragmentManager,
                            fragmentType = mockData.fragmentType
                        )
                    }
                }
            }
        }
    }
}
