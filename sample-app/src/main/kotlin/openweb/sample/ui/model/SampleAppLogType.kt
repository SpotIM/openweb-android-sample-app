package openweb.sample.ui.model

enum class SampleAppLogType(val colorResId: Int) {
    Logcat(android.R.color.holo_blue_dark),
    AnalyticsEvent(android.R.color.holo_green_dark),
    ViewActionCallbacks(android.R.color.holo_orange_dark),
    FlowActionCallbacks(android.R.color.holo_purple),
}
