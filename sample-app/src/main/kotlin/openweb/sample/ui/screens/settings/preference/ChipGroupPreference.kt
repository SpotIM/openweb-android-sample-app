package openweb.sample.ui.screens.settings.preference

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.core.view.forEach
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.preference.R as PreferenceR
import com.google.android.material.chip.Chip
import openweb.sample.R
import openweb.sample.databinding.PreferenceChipGroupBinding

/**
 * Custom preference that displays options as a horizontal chip group
 * Similar to RadioGroup but with Material Design chips
 */
class ChipGroupPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = PreferenceR.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var _binding: PreferenceChipGroupBinding? = null
    private val binding: PreferenceChipGroupBinding get() = _binding!!

    private var entries: Array<CharSequence> = emptyArray()
        set(value) {
            field = value
            notifyChanged()
        }

    private var entryValues: Array<CharSequence> = emptyArray()

    private var selectedValue: String? = null
    private var contentDescriptionPrefix: String? = null

    init {
        layoutResource = R.layout.preference_chip_group

        // Ensure persistence is enabled
        isPersistent = true

        // Read custom attributes using withStyledAttributes (auto-recycles)
        context.withStyledAttributes(attrs, R.styleable.ChipGroupPreference) {
            val entriesResId = getResourceId(R.styleable.ChipGroupPreference_entries, 0)
            if (entriesResId != 0) {
                entries = context.resources.getTextArray(entriesResId)
            }

            val entryValuesResId = getResourceId(R.styleable.ChipGroupPreference_entryValues, 0)
            if (entryValuesResId != 0) {
                entryValues = context.resources.getTextArray(entryValuesResId)
            }

            contentDescriptionPrefix = getString(R.styleable.ChipGroupPreference_contentDescriptionPrefix)
        }
    }

    override fun onGetDefaultValue(a: android.content.res.TypedArray, index: Int): Any? = a.getString(index)

    override fun onSetInitialValue(defaultValue: Any?) = setValue(getPersistedString(defaultValue as? String))

    override fun shouldPersist(): Boolean =
        // Check if this preference should persist values
        isPersistent && key != null

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        _binding = PreferenceChipGroupBinding.bind(holder.itemView)

        // Clear existing chips
        binding.chipGroup.removeAllViews()

        // Create new chips
        entries.forEachIndexed { index, entry ->
            val chipId = index + CHIP_ID_OFFSET
            val chip = Chip(context, null, R.style.SettingsChipStyle).apply {
                id = chipId
                text = entry
                contentDescription = "${contentDescriptionPrefix.orEmpty()}${title?.toString().orEmpty()}$entry"
                    .replace(" ", "")
                isCheckable = true
                isChecked = entryValues.getOrNull(index)?.toString() == selectedValue
                isEnabled = this@ChipGroupPreference.isEnabled // Respect preference enabled state
                setOnClickListener {
                    val newValue = entryValues.getOrNull(index)?.toString() ?: return@setOnClickListener

                    // Don't update UI yet - let the listener decide
                    val shouldUpdate = callChangeListener(newValue)

                    if (shouldUpdate) {
                        // Listener approved - update UI and persist
                        setValueInternal(newValue, true)
                        if (binding.chipGroup.checkedChipId != chipId) {
                            binding.chipGroup.check(chipId)
                        }
                    } else {
                        // Listener rejected - revert chip selection to current value
                        val currentIndex = entryValues.indexOfFirst { it.toString() == selectedValue }
                        if (currentIndex >= 0) {
                            binding.chipGroup.check(currentIndex + CHIP_ID_OFFSET)
                        }
                    }
                }
            }
            binding.chipGroup.addView(chip)
        }

        // Set the currently selected chip based on selectedValue
        val selectedIndex = entryValues.indexOfFirst { it.toString() == selectedValue }
        if (selectedIndex >= 0) {
            binding.chipGroup.check(selectedIndex + CHIP_ID_OFFSET)
        }
    }

    fun setValue(value: String?) {
        val changed = selectedValue != value
        selectedValue = value
        if (changed) {
            // Force UI refresh by calling notifyChanged
            notifyChanged()
        }
    }

    fun getValue(): String? = selectedValue

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        _binding?.chipGroup?.forEach { child ->
            (child as? Chip)?.isEnabled = enabled
        }
    }

    private fun setValueInternal(value: String?, shouldPersist: Boolean) {
        val changed = selectedValue != value
        selectedValue = value

        // Always persist when requested and preference is persistent
        if (shouldPersist && shouldPersist()) {
            persistString(value)
        }

        // Notify changes only if value actually changed
        if (changed) {
            notifyDependencyChange(shouldDisableDependents())
            notifyChanged()
        }
    }

    companion object {
        // Base ID offset for chip IDs to avoid conflicts with other views
        private const val CHIP_ID_OFFSET = 1000
    }
}
