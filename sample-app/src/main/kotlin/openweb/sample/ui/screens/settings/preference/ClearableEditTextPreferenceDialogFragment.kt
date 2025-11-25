package openweb.sample.ui.screens.settings.preference

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.EditTextPreferenceDialogFragmentCompat
import com.google.android.material.textfield.TextInputLayout

class ClearableEditTextPreferenceDialogFragment : EditTextPreferenceDialogFragmentCompat() {

    private var textInputLayout: TextInputLayout? = null

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        // Find the TextInputLayout
        textInputLayout = findTextInputLayout(view)

        val editText = textInputLayout?.editText ?: return
        val preference = preference as? ClearableEditTextPreference ?: return

        // Add text watcher for validation
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                validateAndUpdateButton(s?.toString() ?: "", preference)
            }
        })

        // Validate initial text
        validateAndUpdateButton(editText.text?.toString() ?: "", preference)
    }

    override fun onStart() {
        super.onStart()

        // Validate again when dialog starts to ensure button state is correct
        val editText = textInputLayout?.editText
        val preference = preference as? ClearableEditTextPreference
        if (editText != null && preference != null) {
            validateAndUpdateButton(editText.text?.toString() ?: "", preference)
        }
    }

    private fun validateAndUpdateButton(text: String, preference: ClearableEditTextPreference) {
        val errorMessage = preference.validator?.invoke(text)
        val isValid = errorMessage == null

        // Update error message in TextInputLayout
        textInputLayout?.error = errorMessage
        textInputLayout?.isErrorEnabled = !isValid

        // Enable/disable positive button with visual feedback
        (dialog as? AlertDialog)?.getButton(DialogInterface.BUTTON_POSITIVE)?.let { button ->
            button.isEnabled = isValid
            // Add visual feedback: reduce alpha when disabled
            button.alpha = if (isValid) 1.0f else 0.4f
        }
    }

    private fun findTextInputLayout(view: View): TextInputLayout? {
        if (view is TextInputLayout) return view
        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                val found = findTextInputLayout(view.getChildAt(i))
                if (found != null) return found
            }
        }
        return null
    }

    companion object {
        fun newInstance(key: String): ClearableEditTextPreferenceDialogFragment =
            ClearableEditTextPreferenceDialogFragment().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
    }
}
