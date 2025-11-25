package openweb.sample.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import spotIm.common.api.model.localization.OWLanguageStrategy
import spotIm.common.api.model.localization.OWLanguageStrategy.Custom
import spotIm.common.api.model.localization.OWLanguageStrategy.Device
import spotIm.common.api.model.localization.OWLanguageStrategy.ServerConfig
import spotIm.common.api.model.localization.OWLocaleStrategy
import spotIm.common.api.model.localization.OWSupportedLanguage
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle.Floating
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle.Light
import spotIm.common.api.model.settings.commentcreation.styles.OWCommentCreationStyle.Regular

inline fun <reified T : Any> T?.toJson(prettyPrinting: Boolean = false): String? {
    if (this == null) return null
    val json = if (prettyPrinting) {
        Json {
            prettyPrint = true
            prettyPrintIndent = "    "
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
        }
    } else {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
        }
    }
    return try {
        json.encodeToString(this)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T : Any> String?.fromJson(): T? {
    if (this.isNullOrEmpty()) return null
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }
    return try {
        json.decodeFromString<T>(this)
    } catch (e: Exception) {
        null
    }
}

fun OWLanguageStrategy.Companion.fromString(strategy: String): OWLanguageStrategy {
    return if (strategy.startsWith("Custom(language=")) {
        val languageName = strategy
            .removePrefix("Custom(language=")
            .removeSuffix(")")
        val language = OWSupportedLanguage.entries.firstOrNull { it.name == languageName }
        if (language != null) Custom(language) else Device
    } else {
        when (strategy) {
            Device.toString() -> Device
            ServerConfig.toString() -> ServerConfig
            else -> Device
        }
    }
}

fun OWCommentCreationStyle.Companion.fromString(style: String): OWCommentCreationStyle {
    return when (style) {
        Regular.toString() -> Regular
        Light.toString() -> Light
        Floating.toString() -> Floating
        else -> Regular
    }
}

fun OWLocaleStrategy.Companion.valueOf(strategy: String): OWLocaleStrategy {
    return when (strategy) {
        OWLocaleStrategy.Device.toString() -> OWLocaleStrategy.Device
        OWLocaleStrategy.ServerConfig.toString() -> OWLocaleStrategy.ServerConfig
        else -> OWLocaleStrategy.Device
    }
}

fun <T> AppCompatActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> View.collectFlowWithLifecycle(flow: Flow<T>, collect: suspend (T) -> Unit) {
    val lifecycleOwner = findViewTreeLifecycleOwner() ?: return
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun String.isValidBaseUrl() = this.isNotEmpty() && (this.startsWith("http://") || this.startsWith("https://"))
