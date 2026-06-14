package openweb.sample.ui.screens.settings

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

/**
 * Helper class to build and show dialogs for Settings screen.
 * Separates dialog construction logic from Fragment.
 */
object SettingsDialogHelper {

    /**
     * Shows a confirmation dialog with custom title, message, and actions
     */
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = "Restart",
        onConfirm: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onConfirm() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Shows a toast message
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
