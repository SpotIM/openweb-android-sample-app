package openweb.sample.ui.screens.settings

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

/**
 * Helper class to build and show dialogs for Settings screen.
 * Separates dialog construction logic from Fragment.
 */
object SettingsDialogHelper {

    /**
     * Shows a color picker dialog for selecting custom dark color
     */
    fun showColorPickerDialog(
        context: Context,
        onColorSelected: (Int) -> Unit,
        onReset: () -> Unit
    ) {
        val dialog = ColorPickerDialog.Builder(context)
            .setTitle("Pick Dark Color")
            .setPreferenceName("CustomDarkColorPicker")
            .setPositiveButton(
                "Apply",
                ColorEnvelopeListener { envelope, _ ->
                    envelope?.let { onColorSelected(it.color) }
                }
            )
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .create()

        // Add Reset button using neutral button
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reset") { _, _ -> onReset() }

        dialog.show()
    }

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
