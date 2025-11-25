package openweb.sample.utils

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.giphy.sdk.core.models.Image
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import spotIm.common.api.ui.gif.GifSelectionListener
import spotIm.common.api.ui.gif.GiphyImage
import spotIm.common.api.ui.gif.GiphyMedia
import spotIm.common.api.ui.gif.GiphyRating
import spotIm.common.api.ui.gif.GiphySetting
import spotIm.common.api.ui.gif.GiphyTheme
import spotIm.common.api.ui.gif.SpotGiphyProvider

object GiphyHandler : SpotGiphyProvider {
    override fun configure(activityContext: Context, sdkKey: String) {
        Giphy.configure(activityContext, sdkKey)
    }

    override fun showGiphyDialogFragment(
        giphySetting: GiphySetting,
        fragmentManager: FragmentManager,
        fragmentTag: String,
        selectionListener: GifSelectionListener
    ) {
        val theme = when (giphySetting.theme) {
            GiphyTheme.DARK -> GPHTheme.Dark
            GiphyTheme.LIGHT -> GPHTheme.Light
        }
        val rating = when (giphySetting.rating) {
            GiphyRating.Y -> RatingType.y
            GiphyRating.G -> RatingType.g
            GiphyRating.R -> RatingType.r
            GiphyRating.PG -> RatingType.pg
            GiphyRating.PG13 -> RatingType.pg13
        }

        val settings = GPHSettings(rating = rating, theme = theme)
        val gifsDialog = GiphyDialogFragment.newInstance(settings)

        gifsDialog.gifSelectionListener =
            object : GiphyDialogFragment.GifSelectionListener {

                private fun toGiphyImage(image: Image?): GiphyImage? {
                    return image?.let {
                        GiphyImage(it.gifUrl, it.webPUrl, it.height, it.width)
                    }
                }

                private fun toGiphyMedia(media: Media): GiphyMedia {
                    return GiphyMedia(
                        original = toGiphyImage(media.images.original),
                        preview = toGiphyImage(media.images.preview),
                        title = media.title.orEmpty()
                    )
                }

                override fun didSearchTerm(term: String) {
                    // Callback for search terms
                }

                override fun onDismissed(selectedContentType: GPHContentType) {
                    // Your user dismissed the dialog without selecting a GIF
                }

                override fun onGifSelected(
                    media: Media,
                    searchTerm: String?,
                    selectedContentType: GPHContentType
                ) {
                    selectionListener.onGifSelected(toGiphyMedia(media), searchTerm)
                }
            }
        gifsDialog.show(fragmentManager, fragmentTag)
    }
}
