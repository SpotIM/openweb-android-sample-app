package openweb.sample.ui.screens.verticals.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import openweb.sample.ui.screens.verticals.components.article.ArticleContent
import openweb.sample.ui.screens.verticals.components.article.BottomSheetFragmentContainer
import openweb.sample.ui.screens.verticals.components.article.ImplementationInfoCard
import openweb.sample.ui.screens.verticals.components.article.VerticalTopAppBar
import openweb.sample.ui.screens.verticals.model.VerticalMockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiderailVerticalScreen(
    mockData: VerticalMockData,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val viewModel: SiderailVerticalScreenContract = koinViewModel<SiderailVerticalScreenVM> { parametersOf(mockData) }
    val uiState by viewModel.outputs.uiState.collectAsStateWithLifecycle()
    val settingsVersion by viewModel.outputs.settingsVersion.collectAsStateWithLifecycle()
    val mockData = viewModel.outputs.mockData
    val scrollState = rememberScrollState()
    val conversationParams = remember { viewModel.inputs.buildConversationParams() }

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

    BackHandler(enabled = uiState.isDrawerOpen) {
        viewModel.inputs.onDrawerToggle()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                VerticalTopAppBar(
                    title = mockData.title,
                    containerColor = mockData.color,
                    onBackClick = onBackClick,
                    onSettingsClick = onSettingsClick,
                    showConversationButton = true,
                    onConversationClick = { viewModel.inputs.onDrawerToggle() }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
            ) {
                ArticleContent(article = mockData.article, brandColor = mockData.color)

                ImplementationInfoCard(
                    info = mockData.implementationInfo,
                    expanded = uiState.implementationInfoExpanded,
                    onToggle = { viewModel.inputs.onImplementationInfoClicked() },
                    iconColor = mockData.color
                )
            }
        }

        // Scrim overlay when drawer is open
        AnimatedVisibility(
            visible = uiState.isDrawerOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.inputs.onDrawerToggle()
                    }
            )
        }

        // Drawer from right side
        AnimatedVisibility(
            visible = uiState.isDrawerOpen,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp
            ) {
                key(settingsVersion) {
                    BottomSheetFragmentContainer(
                        params = conversationParams,
                        fragmentManager = fragmentManager
                    )
                }
            }
        }
    }
}
