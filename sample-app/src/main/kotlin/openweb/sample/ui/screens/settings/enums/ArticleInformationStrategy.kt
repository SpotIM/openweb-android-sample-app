package openweb.sample.ui.screens.settings.enums

/**
 * Defines where article metadata is sourced for SDK initialization.
 *
 * - [Server]: Article information retrieved from OpenWeb servers (production use)
 * - [Local]: Article information provided locally from app code (testing/demo)
 */
enum class ArticleInformationStrategy {
    Server,
    Local
}
