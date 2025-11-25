package openweb.sample.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View?.hideKeyboard() {
    this ?: return

    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

private const val DEFAULT_DEBOUNCE_TIME_MS = 750L

/**
 * Adds a debounced click listener to the View.
 * This is useful for preventing multiple rapid clicks triggering multiple actions.
 *
 * @param debounceTime The time in milliseconds to debounce clicks. Defaults to 750ms.
 * @param onClick The lambda to execute when the debounced click is detected.
 *               Pass null to remove the click listener.
 *
 * Example usage:
 * ```
 * button.setDebouncedClickListener {
 *     // This will only execute once even if button is clicked multiple times quickly
 *     performAction()
 * }
 * ```
 *
 * Note: Uses a WeakReference to prevent potential memory leaks with the click callback.
 */
fun View.setDebouncedClickListener(
    debounceTime: Long = DEFAULT_DEBOUNCE_TIME_MS,
    onClick: (() -> Unit)? = null
) {
    if (onClick == null) {
        setOnClickListener(null)
        return
    }

    var lastClickTime = 0L

    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick.invoke()
        }
    }
}

fun Activity.supportEdgeToEdge(view: View) {
    view.applyInsets()
    configureStatusBarIconsColor(isDarkModeEnabled())
}

fun View.applyInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

        // Ensure the bottom padding considers both the keyboard and navigation bar
        val bottomInset = maxOf(imeInsets.bottom, systemBars.bottom)

        view.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomInset)
        WindowInsetsCompat.CONSUMED
    }
}

fun Activity.configureStatusBarIconsColor(isDarkMode: Boolean) {
    if (isDarkMode) {
        showLightStatusBarIcons()
    } else {
        showDarkStatusBarIcons()
    }
}

fun Activity.isDarkModeEnabled(): Boolean =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun Activity.showLightStatusBarIcons() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For API level 30 and above
        window.insetsController?.setSystemBarsAppearance(
            0, // Clears the light status bar appearance flag
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        // For backward compatibility with older APIs
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun Activity.showDarkStatusBarIcons() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For API level 30 and above
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        // For backward compatibility with older APIs
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
