package openweb.sample.utils

import openweb.sample.BuildConfig

object BuildUtils {
    fun isInternalBuild(): Boolean = BuildConfig.FLAVOR_ow_sdk == "internal"
    fun isRnBuild(): Boolean = BuildConfig.FLAVOR_platform == "rn"
}
