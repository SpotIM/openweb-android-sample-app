@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package openweb.sample.ui.screens.verticals.components.article

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import spotIm.common.api.callbacks.SpotCallback
import spotIm.common.api.exceptions.SpotException
import openweb.sample.R
import openweb.sample.ui.screens.verticals.model.ConversationParams
import openweb.sample.ui.screens.verticals.model.FragmentType
import openweb.sample.utils.logger.SampleLogger
import spotIm.common.api.model.customizations.UIColor
import spotIm.sdk.OpenWeb

@Composable
fun ConversationFragmentContainer(
    params: ConversationParams,
    fragmentManager: FragmentManager,
    fragmentType: FragmentType
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            FragmentContainerView(context).apply {
                id = R.id.fragment_container_view_id
                post {
                    loadFragment(this, fragmentManager, params, fragmentType, params.brandColor)
                }
            }
        }
    )
}

@Composable
fun BottomSheetFragmentContainer(
    params: ConversationParams,
    fragmentManager: FragmentManager
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FragmentContainerView(context).apply {
                id = R.id.bottom_sheet_fragment_container_view_id
                post {
                    loadFragment(this, fragmentManager, params, FragmentType.Conversation, params.brandColor)
                }
            }
        }
    )
}

private fun loadFragment(
    view: FragmentContainerView,
    fragmentManager: FragmentManager,
    params: ConversationParams,
    fragmentType: FragmentType,
    brandColor: Color
) {
    updateBrandColor(brandColor)

    val callback = object : SpotCallback<Fragment> {
        override fun onSuccess(response: Fragment) {
            if (view.isAttachedToWindow) {
                fragmentManager.beginTransaction()
                    .replace(view.id, response)
                    .commit()
            }
        }

        override fun onFailure(exception: SpotException) {
            SampleLogger.e(
                tag = "VerticalScreen",
                message = "Failed to load conversation fragment",
                e = exception
            )
        }
    }

    when (fragmentType) {
        FragmentType.Conversation -> {
            OpenWeb.manager.ui.flows.fragments.getConversation(
                postId = params.postId,
                articleSettings = params.articleSettings,
                additionalSettings = params.additionalSettings,
                flowActionsCallback = null,
                callback = callback
            )
        }

        FragmentType.PreConversation -> {
            OpenWeb.manager.ui.flows.fragments.getPreConversation(
                postId = params.postId,
                articleSettings = params.articleSettings,
                additionalSettings = params.additionalSettings,
                flowActionsCallback = null,
                callback = callback
            )
        }
    }
}

private fun updateBrandColor(brandColor: Color) {
    OpenWeb.manager.ui.customizations.customizedTheme.brandColor = UIColor(
        lightColor = brandColor.toArgb(),
        darkColor = brandColor.toArgb()
    )
}
