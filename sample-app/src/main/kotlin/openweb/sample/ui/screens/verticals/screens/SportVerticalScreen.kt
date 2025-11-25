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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import openweb.sample.ui.screens.examples.compose.ui.rememberKeyboardHeight
import openweb.sample.ui.screens.verticals.components.article.ConversationFragmentContainer
import openweb.sample.ui.screens.verticals.components.article.ImplementationInfoCard
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
    val keyboardHeight = rememberKeyboardHeight()
    val lazyListState = rememberLazyListState()
    val rootView = LocalView.current
    var isKeyboardVisible by remember { mutableStateOf(false) }

    val activity = LocalContext.current as FragmentActivity
    val fragmentManager = activity.supportFragmentManager

    LaunchedEffect(Unit) {
        viewModel.inputs.initializeBeforeFragment()
    }

    // Detect when screen resumes (e.g., returning from settings)
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

    // Listen to IME visibility changes directly from root view
    DisposableEffect(rootView) {
        val listener = android.view.ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(rootView)
            val imeVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            if (imeVisible != isKeyboardVisible) {
                isKeyboardVisible = imeVisible
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    // Scroll to bottom when keyboard becomes visible
    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            val lastIndex = lazyListState.layoutInfo.totalItemsCount - 1
            if (lastIndex >= 0) {
                lazyListState.animateScrollToItem(lastIndex)
            }
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
            // Static scoreboard — always visible below top bar, does NOT scroll
            SportScoreboard(
                modifier = Modifier.zIndex(1f),
                homeTeamName = "Home Team",
                awayTeamName = "Away Team",
                homeScore = uiState.homeScore,
                awayScore = uiState.awayScore,
                matchMinute = uiState.matchMinute,
                isLive = uiState.isLive,
                goalEvent = uiState.goalEvent
            )

            // Scrollable content below the scoreboard
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    bottom = padding.calculateBottomPadding() + keyboardHeight.value
                )
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
                    key(settingsVersion) {
                        ConversationFragmentContainer(
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
