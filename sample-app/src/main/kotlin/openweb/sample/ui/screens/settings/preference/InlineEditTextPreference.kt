package openweb.sample.ui.screens.settings.preference

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.preference.R as PreferenceR
import openweb.sample.databinding.PreferenceInlineEdittextBinding

/**
 * EditText preference that displays inline without opening a dialog
 */
class InlineEditTextPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = PreferenceR.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var _binding: PreferenceInlineEdittextBinding? = null
    private val binding: PreferenceInlineEdittextBinding get() = _binding!!

    private var currentValue: String? = null
    private var textWatcher: TextWatcher? = null

    var validator: ((String) -> String?)? = null // Returns error message or null if valid

    init {
        layoutResource = openweb.sample.R.layout.preference_inline_edittext
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        _binding = PreferenceInlineEdittextBinding.bind(holder.itemView)

        // Remove old text watcher if exists
        textWatcher?.let { binding.editText.removeTextChangedListener(it) }

        // Set current value
        binding.editText.setText(currentValue)

        // Set hint from summary
        binding.editText.hint = summary

        // Add text watcher to persist changes and validate
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()

                // Validate and show error
                val (_, errorMessage) = validateText(text)
                binding.editText.error = errorMessage

                // Always persist, regardless of validation state
                val newValue = text.takeIf { it.isNotEmpty() }
                if (callChangeListener(newValue)) {
                    setText(newValue)
                }
            }
        }
        binding.editText.addTextChangedListener(textWatcher)
    }

    fun setText(text: String?) {
        currentValue = text
        persistString(text)
    }

    fun getText(): String? = currentValue

    /**
     * Validates a specific text value using the validator
     * @return Pair of (isValid, errorMessage). errorMessage is null when valid.
     */
    private fun validateText(text: String): Pair<Boolean, String?> {
        val errorMessage = validator?.invoke(text)
        return Pair(errorMessage == null, errorMessage)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        currentValue = getPersistedString(defaultValue as? String)
    }
}
