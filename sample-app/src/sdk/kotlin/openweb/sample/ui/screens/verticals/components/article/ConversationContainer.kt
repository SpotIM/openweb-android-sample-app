package openweb.sample.ui.screens.verticals.components.article

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentManager
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.FragmentType
import openweb.sample.utils.logger.SampleLogger
import spotIm.common.api.model.customizations.OWCustomizationElement
import spotIm.common.api.model.customizations.UIColor
import spotIm.common.api.model.settings.conversation.OWConversationNavigationOptions
import spotIm.compose.Conversation
import spotIm.compose.PreConversation
import spotIm.sdk.OpenWeb

@Composable
@Suppress("UNUSED_PARAMETER")
fun ConversationContainer(
    params: ConversationParams,
    fragmentManager: FragmentManager,
    fragmentType: FragmentType
) {
    SideEffect { updateBrandColor(params.brandColor) }

    val containerModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()

    when (fragmentType) {
        FragmentType.Conversation ->
            Conversation(
                postId = params.postId,
                modifier = containerModifier,
                articleSettings = params.articleSettings,
                additionalSettings = params.additionalSettings,
                onError = { SampleLogger.e(message = "Failed to load conversation", tag = TAG, e = it) }
            )

        FragmentType.PreConversation ->
            PreConversation(
                postId = params.postId,
                modifier = containerModifier,
                articleSettings = params.articleSettings,
                additionalSettings = params.additionalSettings,
                conversationNavigationOptions = OWConversationNavigationOptions.ConversationFullScreen(),
                onError = { SampleLogger.e(message = "Failed to load pre-conversation", tag = TAG, e = it) }
            )
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
fun BottomSheetContainer(
    params: ConversationParams,
    fragmentManager: FragmentManager
) {
    SideEffect { updateBrandColor(params.brandColor) }

    Conversation(
        postId = params.postId,
        modifier = Modifier.fillMaxSize(),
        articleSettings = params.articleSettings,
        additionalSettings = params.additionalSettings,
        onError = { SampleLogger.e(message = "Failed to load conversation (bottom sheet)", tag = TAG, e = it) }
    )
}

private fun updateBrandColor(brandColor: Color) {
    OpenWeb.manager.ui.customizations.elements.brand.color =
        UIColor(
            lightColor = brandColor.toArgb(),
            darkColor = brandColor.toArgb()
        )
}

private const val TAG = "VerticalScreen"
