package openweb.sample.ui.screens.verticals.screens

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import openweb.sample.ui.screens.verticals.components.article.BottomSheetFragmentContainer
import openweb.sample.ui.screens.verticals.components.video.ImplementationInfoOverlay
import openweb.sample.ui.screens.verticals.components.video.VideoItem
import openweb.sample.ui.screens.verticals.model.VerticalMockData
import openweb.sample.utils.ThemeHelper

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun VideoVerticalScreen(
    mockData: VerticalMockData,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val themeHelper: ThemeHelper = koinInject()
    val viewModel: VideoVerticalScreenContract = koinViewModel<VideoVerticalScreenVM> {
        parametersOf(mockData)
    }
    val uiState by viewModel.outputs.uiState.collectAsStateWithLifecycle()
    val settingsVersion by viewModel.outputs.settingsVersion.collectAsStateWithLifecycle()

    val activity = context as FragmentActivity
    val fragmentManager = activity.supportFragmentManager

    DisposableEffect(Unit) {
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }

    val conversationParams = remember {
        viewModel.inputs.buildConversationParams()
    }

    val videos = remember(uiState.videoUrls) {
        uiState.videoUrls
    }
    val commentCountLabel = uiState.commentCount?.toString().orEmpty()

    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    LaunchedEffect(Unit) {
        viewModel.inputs.initializeBeforeFragment()
    }

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    var fragmentContainerKey by remember { mutableIntStateOf(0) }

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

    LaunchedEffect(uiState.isBottomSheetVisible, settingsVersion) {
        if (uiState.isBottomSheetVisible) {
            fragmentContainerKey++
            bottomSheetState.expand()
        } else if (bottomSheetState.currentValue != SheetValue.Hidden) {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue != SheetValue.Expanded && uiState.isBottomSheetVisible) {
            viewModel.inputs.onBottomSheetDismissed()
        }
    }

    BackHandler(enabled = uiState.isBottomSheetVisible) {
        viewModel.inputs.onBottomSheetDismissed()
    }

    BackHandler(enabled = uiState.isInfoDialogVisible) {
        viewModel.inputs.onInfoDialogDismissed()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContainerColor = themeHelper.getBottomSheetBackgroundColor(context, uiState.isBottomSheetVisible),
        sheetContent = {
            if (uiState.isBottomSheetVisible) {
                key(fragmentContainerKey) {
                    Box(modifier = Modifier.fillMaxHeight(0.9f)) {
                        BottomSheetFragmentContainer(
                            params = conversationParams,
                            fragmentManager = fragmentManager
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }
        },
        modifier = modifier
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val actualIndex = page % videos.size
            VideoItem(
                videoUrl = videos[actualIndex],
                isVisible = page == pagerState.currentPage,
                onBackClick = onBackClick,
                commentCountLabel = commentCountLabel,
                onCommentClick = { viewModel.inputs.onCommentButtonClicked() },
                onInfoClick = { viewModel.inputs.onInfoButtonClicked() }
            )
        }

        if (uiState.isInfoDialogVisible) {
            ImplementationInfoOverlay(
                info = mockData.implementationInfo,
                onDismiss = { viewModel.inputs.onInfoDialogDismissed() }
            )
        }
    }
}
