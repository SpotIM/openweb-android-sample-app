package openweb.sample.ui.screens.settings.preference

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import androidx.preference.R as PreferenceR
import com.google.android.material.textfield.TextInputLayout
import openweb.sample.R

/**
 * EditTextPreference with Material Design TextInputLayout, clear button, and validation
 */
class ClearableEditTextPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = PreferenceR.attr.editTextPreferenceStyle
) : EditTextPreference(context, attrs, defStyleAttr) {

    var validator: ((String) -> String?)? = null // Returns error message or null if valid

    init {
        dialogLayoutResource = R.layout.preference_dialog_edittext_material

        setOnBindEditTextListener { editText ->
            // Find the TextInputLayout parent
            val parent = editText.parent?.parent as? TextInputLayout
            parent?.let { textInputLayout ->
                // Enable clear button (end icon)
                textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT

                // Set hint
                textInputLayout.hint = dialogMessage ?: title
            }

            // Set input type if specified
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        }
    }
}
