package openweb.sample.ui.screens.verticals.model

data class SportVerticalUiState(
    val implementationInfoExpanded: Boolean = false,
    val reactionsExpanded: Boolean = true,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val matchMinute: Int = 0,
    val isLive: Boolean = true,
    val goalEvent: GoalEvent? = null
)

data class GoalEvent(
    val teamName: String,
    val id: Int
)
