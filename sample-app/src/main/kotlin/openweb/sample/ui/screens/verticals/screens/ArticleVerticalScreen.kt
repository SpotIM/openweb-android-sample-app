package openweb.sample.ui.screens.verticals.screens

import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import openweb.sample.ui.screens.examples.compose.ui.rememberKeyboardHeight
import openweb.sample.ui.screens.verticals.components.article.ArticleContent
import openweb.sample.ui.screens.verticals.components.article.ConversationFragmentContainer
import openweb.sample.ui.screens.verticals.components.article.ImplementationInfoCard
import openweb.sample.ui.screens.verticals.components.article.VerticalTopAppBar
import openweb.sample.ui.screens.verticals.model.VerticalMockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleVerticalScreen(
    mockData: VerticalMockData,
    modifier: Modifier = Modifier,
    customContent: (@Composable () -> Unit)? = null,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val viewModel: ArticleVerticalScreenContract = koinViewModel<ArticleVerticalScreenVM> { parametersOf(mockData) }
    val uiState by viewModel.outputs.uiState.collectAsStateWithLifecycle()
    val mockData = viewModel.outputs.mockData
    val settingsVersion by viewModel.outputs.settingsVersion.collectAsStateWithLifecycle()
    val keyboardHeight = rememberKeyboardHeight()
    val scrollState = rememberScrollState()
    val rootView = LocalView.current
    var isKeyboardVisible by remember { mutableStateOf(false) }

    val activity = LocalContext.current as FragmentActivity
    val fragmentManager = activity.supportFragmentManager

    LaunchedEffect(Unit) {
        viewModel.inputs.initializeBeforeFragment()
    }

    // Detect when screen resumes (e.g., returning from settings)
    DisposableEffect(Unit) {
        val lifecycleObserver = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.inputs.onScreenResumed()
            }
        }
        activity.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    // Listen to IME visibility changes directly from root view
    LaunchedEffect(rootView) {
        val listener = { view: View ->
            val insets = ViewCompat.getRootWindowInsets(view)
            val imeVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            if (imeVisible != isKeyboardVisible) {
                isKeyboardVisible = imeVisible
            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            listener(rootView)
        }
    }

    // Scroll to bottom when keyboard becomes visible
    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            VerticalTopAppBar(
                title = mockData.title,
                containerColor = mockData.color,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = keyboardHeight.value)
                .verticalScroll(scrollState)
        ) {
            ArticleContent(article = mockData.article)

            customContent?.invoke()

            ImplementationInfoCard(
                info = mockData.implementationInfo,
                expanded = uiState.implementationInfoExpanded,
                onToggle = { viewModel.inputs.onImplementationInfoClicked() },
                iconColor = mockData.color
            )

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
