package openweb.sample.ui.screens.settings.enums

/**
 * Simulated renewSSO() host behaviors, used to exercise the SDK's 403 recovery and SSO renewal
 * paths without needing a misbehaving real host.
 *
 * - [Normal]: implements renewSSO(), replays the last SSO login, and calls onComplete when done.
 * - [NotImplemented]: leaves renewSSOAuthentication unset, so the SDK has to recover on its own.
 * - [NeverAnswers]: the callback is set but performs no work and never calls onComplete.
 * - [RenewsButNeverReports]: performs the full SSO login like [Normal], but never calls onComplete
 *   leaving a healthy session that the SDK still believes failed to renew.
 * - [AnswersWithoutRenewing]: calls onComplete immediately without performing any SSO login.
 */
enum class RenewSSOMode {
    Normal,
    NotImplemented,
    NeverAnswers,
    RenewsButNeverReports,
    AnswersWithoutRenewing
}
