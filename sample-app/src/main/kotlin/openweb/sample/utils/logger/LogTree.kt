package openweb.sample.utils.logger

import android.util.Log
import timber.log.Timber

/**
 * Logger tree for Timber.
 *
 * @author Moshe Waisberg
 */
open class LogTree(private val debug: Boolean) : Timber.DebugTree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean =
        (debug || priority >= Log.INFO) && super.isLoggable(tag, priority)
}
