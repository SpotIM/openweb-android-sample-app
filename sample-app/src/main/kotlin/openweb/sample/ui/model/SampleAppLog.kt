package openweb.sample.ui.model

import java.util.UUID

data class SampleAppLog(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val type: SampleAppLogType,
    val timestamp: Long = System.currentTimeMillis()
)

fun List<SampleAppLog>.applyFilters(
    filterTypes: List<SampleAppLogType>?,
    searchQuery: String
): List<SampleAppLog> {
    var filteredLogs = if (filterTypes == null) {
        this
    } else {
        this.filter { it.type in filterTypes }
    }

    if (searchQuery.isNotEmpty()) {
        filteredLogs = filteredLogs.filter {
            it.message.contains(searchQuery, ignoreCase = true)
        }
    }

    return filteredLogs
}
