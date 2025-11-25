package openweb.sample.utils.logger

import openweb.sample.BuildConfig
import openweb.sample.ui.features.floatinglogger.FloatingLoggerManager
import openweb.sample.utils.logger.SampleLogLevel.DEBUG
import openweb.sample.utils.logger.SampleLogLevel.ERROR
import openweb.sample.utils.logger.SampleLogLevel.INFO
import openweb.sample.utils.logger.SampleLogLevel.VERBOSE
import openweb.sample.utils.logger.SampleLogLevel.WARN
import timber.log.Timber

@Suppress("FunctionMinLength")
object SampleLogger {
    private const val LOG_TAG = "SampleApp"
    private var isFloatingLoggerTreePlanted = false

    init {
        Timber.plant(LogTree(BuildConfig.DEBUG))
        Timber.plant(CrashlyticsTree())
    }

    fun plantFloatingLoggerTree(floatingLoggerManager: FloatingLoggerManager) {
        if (!isFloatingLoggerTreePlanted) {
            Timber.plant(FloatingLoggerTree(floatingLoggerManager))
            isFloatingLoggerTreePlanted = true
        }
    }

    private fun log(logLevel: SampleLogLevel, tag: String? = null, message: String, e: Throwable? = null) {
        val ltag = if (tag != null) "${LOG_TAG}_$tag" else LOG_TAG
        when (logLevel) {
            DEBUG -> Timber.tag(ltag).d(e, message)
            ERROR -> Timber.tag(ltag).e(e, message)
            INFO -> Timber.tag(ltag).i(e, message)
            VERBOSE -> Timber.tag(ltag).v(e, message)
            WARN -> Timber.tag(ltag).w(e, message)
        }
    }

    fun d(message: String, tag: String? = null, e: Throwable? = null) = log(DEBUG, tag, message, e)
    fun e(message: String, tag: String? = null, e: Throwable? = null) = log(ERROR, tag, message, e)
    fun i(message: String, tag: String? = null, e: Throwable? = null) = log(INFO, tag, message, e)
    fun v(message: String, tag: String? = null, e: Throwable? = null) = log(VERBOSE, tag, message, e)
    fun w(message: String, tag: String? = null, e: Throwable? = null) = log(WARN, tag, message, e)
}
