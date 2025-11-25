package openweb.sample.utils

import spotIm.common.api.model.Article

@Suppress("MaxLineLength")
fun Article.Companion.mock(): Article {
    val url = "https://test.com"
    val imageUrl = "https://53.fs1.hubspotusercontent-na1.net/hub/53/hubfs/parts-url.jpg?width=595&height=400&name=parts-url.jpg"
    val title = "This is a placeholder for the article title. The container is limited to two lines of text to avoid interface overwhelming but will show the context"
    val subtitle = "News Category"

    return Article(
        url = url,
        thumbnailUrl = imageUrl,
        title = title,
        subtitle = subtitle
    )
}
