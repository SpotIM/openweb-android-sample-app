package openweb.sample.utils.logger

import openweb.sample.ui.features.floatinglogger.FloatingLoggerManager
import openweb.sample.ui.model.SampleAppLogType
import timber.log.Timber

class FloatingLoggerTree(
    private val floatingLoggerManager: FloatingLoggerManager
) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        floatingLoggerManager.addLog(message, SampleAppLogType.Logcat)
    }
}
