package openweb.sample.ui.screens.settings.elementcustomizations

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import openweb.sample.databinding.ItemElementCustomizationBinding
import openweb.sample.ui.screens.settings.customtheme.CustomColorCheckBox
import openweb.sample.utils.setDebouncedClickListener
import spotIm.common.api.model.customizations.OWFontWeight

interface ElementCustomizationsListener {
    fun onCallbackToggled(elementKey: String, enabled: Boolean)
    fun onFontFamilyChanged(elementKey: String, family: String?)
    fun onFontWeightChanged(elementKey: String, weight: OWFontWeight?)
    fun onColorToggled(colorKey: String, enabled: Boolean)
    fun onColorClicked(colorKey: String, isDark: Boolean)
    fun onColorCleared(colorKey: String, isDark: Boolean)
    fun onShuffleClicked(colorKey: String)
    fun onFontInputChanged(elementKey: String, text: String)
    fun onEnterCustomFontMode(elementKey: String)
}

class ElementCustomizationsAdapter(
    fontFamilyOptions: List<Pair<String, String?>>,
    private val listener: ElementCustomizationsListener
) : ListAdapter<ElementCustomizationSetting, ElementCustomizationsAdapter.ViewHolder>(DiffCallback()) {

    /** Keys of cards currently expanded by the user. */
    private val expandedKeys = mutableSetOf<String>()

    private val fontFamilyLabels = fontFamilyOptions.map { it.first }
    private val fontFamilyValues = fontFamilyOptions.map { it.second }

    fun expandAll(keys: List<String>) {
        val oldKeys = expandedKeys.toSet()
        expandedKeys.clear()
        expandedKeys.addAll(keys)
        currentList.forEachIndexed { i, item ->
            if ((item.elementKey in oldKeys) != (item.elementKey in expandedKeys)) {
                notifyItemChanged(i)
            }
        }
    }

    fun collapseAll() {
        val oldKeys = expandedKeys.toSet()
        expandedKeys.clear()
        currentList.forEachIndexed { i, item ->
            if (item.elementKey in oldKeys) notifyItemChanged(i)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            binding = ItemElementCustomizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position).elementKey.hashCode().toLong()

    inner class ViewHolder(
        private val binding: ItemElementCustomizationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // Flag to suppress spinner/switch callbacks during bind
        private var isBinding = false

        init {
            setupSpinner(
                spinner = binding.fontFamilySpinner,
                labels = fontFamilyLabels,
                onSelected = { key, position ->
                    val value = fontFamilyValues[position]
                    if (value == CUSTOM_FONT_SENTINEL) listener.onEnterCustomFontMode(key)
                    else listener.onFontFamilyChanged(key, value)
                }
            )

            setupSpinner(
                spinner = binding.fontWeightSpinner,
                labels = fontWeightLabels,
                onSelected = { key, position ->
                    listener.onFontWeightChanged(key, fontWeightValues[position])
                }
            )

            // TextWatcher: keep the local map in sync on every keystroke.
            // Does NOT call the VM — prevents per-character rebuildAndFilter.
            binding.fontFamilyInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                override fun afterTextChanged(s: Editable?) {
                    if (isBinding) return
                    val key = getItemKey() ?: return
                    listener.onFontInputChanged(key, s?.toString() ?: "")
                }
            })

            // Focus change: commit the buffered text to the VM.
            binding.fontFamilyInput.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !isBinding) {
                    val key = getItemKey() ?: return@setOnFocusChangeListener
                    val text = binding.fontFamilyInput.text?.toString() ?: ""
                    listener.onFontFamilyChanged(key, if (text.isEmpty()) null else text)
                }
            }

            // IME Done: clear focus → triggers the focus listener above
            binding.fontFamilyInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.fontFamilyInput.clearFocus()
                    true
                } else {
                    false
                }
            }

            // Clear custom font button
            binding.fontFamilyClearBtn.setDebouncedClickListener {
                val key = getItemKey() ?: return@setDebouncedClickListener
                listener.onFontFamilyChanged(key, null)
            }

            // Header row click toggles expand/collapse.
            // Drive the transition directly on the card — do NOT call notifyItemChanged, which
            // would trigger RecyclerView's ItemAnimator and cause the list to jump.
            binding.headerRow.setOnClickListener {
                val key = getItemKey() ?: return@setOnClickListener
                val position = absoluteAdapterPosition
                if (position == RecyclerView.NO_ID.toInt()) return@setOnClickListener

                val expanding = key !in expandedKeys
                if (expanding) expandedKeys.add(key) else expandedKeys.remove(key)

                binding.chevron.animate()
                    .rotation(if (expanding) 90f else 0f)
                    .setDuration(CHEVRON_ANIM_MS)
                    .start()

                if (expanding) {
                    val item = getItem(position)
                    isBinding = true
                    try {
                        bindCallbackSection(item)
                        bindFontSection(item)
                        bindColorSection(item)
                    } finally {
                        isBinding = false
                    }
                    // Fade in: content is ready, just reveal it
                    binding.expandableBody.alpha = 0f
                    binding.expandableBody.visibility = View.VISIBLE
                    binding.expandableBody.animate().alpha(1f).setDuration(CHEVRON_ANIM_MS).start()
                } else {
                    // Fade out then hide
                    binding.expandableBody.animate()
                        .alpha(0f)
                        .setDuration(CHEVRON_ANIM_MS / 2)
                        .withEndAction {
                            binding.expandableBody.visibility = View.GONE
                            binding.expandableBody.alpha = 1f
                        }
                        .start()
                }
            }
        }

        private fun getItemKey(): String? {
            val pos = absoluteAdapterPosition
            return if (pos != RecyclerView.NO_ID.toInt()) {
                binding.elementNameTv.tag as? String
            } else null
        }

        private fun setupSpinner(
            spinner: android.widget.Spinner,
            labels: List<String>,
            onSelected: (key: String, position: Int) -> Unit
        ) {
            spinner.adapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                labels
            ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (isBinding) return
                    val key = getItemKey() ?: return
                    onSelected(key, position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }

        fun bind(item: ElementCustomizationSetting) {
            isBinding = true
            try {
                bindHeader(item)
                val isExpanded = item.elementKey in expandedKeys
                binding.expandableBody.visibility = if (isExpanded) View.VISIBLE else View.GONE
                // Animate chevron: 0° collapsed (pointing right), 90° expanded (pointing down)
                val targetRotation = if (isExpanded) 90f else 0f
                binding.chevron.animate().rotation(targetRotation).setDuration(CHEVRON_ANIM_MS).start()

                if (isExpanded) {
                    bindCallbackSection(item)
                    bindFontSection(item)
                    bindColorSection(item)
                }
            } finally {
                isBinding = false
            }
        }

        private fun bindHeader(item: ElementCustomizationSetting) {
            binding.elementNameTv.text = item.displayName
            binding.elementNameTv.tag = item.elementKey

            binding.tagCallback.visibility = if (item.hasCallback) View.VISIBLE else View.GONE
            binding.tagCallback.alpha = if (item.callbackEnabled == true) 1f else BADGE_DIM_ALPHA

            val fontModified = item.fontFamily != null || item.fontWeight != null
            binding.tagFont.visibility = if (item.hasFontControl) View.VISIBLE else View.GONE
            binding.tagFont.alpha = if (fontModified) 1f else BADGE_DIM_ALPHA

            val colorModified = item.colorNewEnabled == true || item.colorLegacyEnabled == true
            binding.tagColor.visibility = if (item.hasColorSetting) View.VISIBLE else View.GONE
            binding.tagColor.alpha = if (colorModified) 1f else BADGE_DIM_ALPHA

            binding.modifiedDot.visibility = if (item.isModified) View.VISIBLE else View.GONE
        }

        private fun bindCallbackSection(item: ElementCustomizationSetting) {
            if (!item.hasCallback) {
                binding.callbackSection.visibility = View.GONE
                return
            }
            binding.callbackSection.visibility = View.VISIBLE

            binding.callbackSwitch.setOnCheckedChangeListener(null)
            binding.callbackSwitch.isChecked = item.callbackEnabled == true
            binding.callbackStatusTv.text =
                if (item.callbackEnabled == true) "Custom element enabled" else "Using default"

            binding.callbackSwitch.setOnCheckedChangeListener { _, isChecked ->
                listener.onCallbackToggled(item.elementKey, isChecked)
            }
        }

        private fun bindFontSection(item: ElementCustomizationSetting) {
            if (!item.hasFontControl) {
                binding.fontSection.visibility = View.GONE
                return
            }
            binding.fontSection.visibility = View.VISIBLE

            // Determine if we are in custom text-input mode
            val isCustomMode = item.customFontInput != null

            if (isCustomMode) {
                binding.fontFamilySpinner.visibility = View.GONE
                binding.fontFamilyInput.visibility = View.VISIBLE
                binding.fontFamilyClearBtn.visibility = View.VISIBLE
                // Use the VM-owned buffer as source of truth. The buffer is updated on every
                // keystroke via onFontInputChanged, so it is always current — even when
                // item.fontFamily hasn't caught up yet (e.g. mid-typing rebind).
                val localText = item.customFontInput ?: ""
                val current = binding.fontFamilyInput.text?.toString() ?: ""
                if (current != localText) {
                    binding.fontFamilyInput.setText(localText)
                    binding.fontFamilyInput.setSelection(localText.length)
                }
            } else {
                binding.fontFamilySpinner.visibility = View.VISIBLE
                binding.fontFamilyInput.visibility = View.GONE
                binding.fontFamilyClearBtn.visibility = View.GONE

                val familyIndex = fontFamilyValues.indexOf(item.fontFamily)
                binding.fontFamilySpinner.setSelection(if (familyIndex >= 0) familyIndex else 0, false)
            }

            val weightIndex = fontWeightValues.indexOf(item.fontWeight)
            binding.fontWeightSpinner.setSelection(if (weightIndex >= 0) weightIndex else 0, false)
        }

        private fun bindColorSection(item: ElementCustomizationSetting) {
            bindColorRow(
                section = binding.colorNewSection,
                switch = binding.colorNewSwitch,
                lightBox = binding.colorNewLightBox,
                darkBox = binding.colorNewDarkBox,
                shuffleBtn = binding.colorNewShuffleBtn,
                keyTv = binding.modernKeyTv,
                enabled = item.colorNewEnabled,
                lightColor = item.colorNewLight,
                darkColor = item.colorNewDark,
                colorKey = item.modernColorKey,
            )
            bindColorRow(
                section = binding.colorLegacySection,
                switch = binding.colorLegacySwitch,
                lightBox = binding.colorLegacyLightBox,
                darkBox = binding.colorLegacyDarkBox,
                shuffleBtn = binding.colorLegacyShuffleBtn,
                keyTv = binding.legacyKeyTv,
                enabled = item.colorLegacyEnabled,
                lightColor = item.colorLegacyLight,
                darkColor = item.colorLegacyDark,
                colorKey = item.legacyColorKey,
            )
        }

        private fun bindColorRow(
            section: View,
            switch: SwitchCompat,
            lightBox: CustomColorCheckBox,
            darkBox: CustomColorCheckBox,
            shuffleBtn: View,
            keyTv: TextView,
            enabled: Boolean?,
            lightColor: Int?,
            darkColor: Int?,
            colorKey: String?,
        ) {
            if (enabled == null) {
                section.visibility = View.GONE
                return
            }
            section.visibility = View.VISIBLE

            switch.setOnCheckedChangeListener(null)
            switch.isChecked = enabled == true
            switch.isEnabled = lightColor != null && darkColor != null
            switch.setOnCheckedChangeListener { _, isChecked ->
                val key = colorKey ?: return@setOnCheckedChangeListener
                listener.onColorToggled(key, isChecked)
            }

            if (lightColor != null) {
                lightBox.setChecked(true, lightColor)
            } else {
                lightBox.setChecked(false)
            }
            if (darkColor != null) {
                darkBox.setChecked(true, darkColor)
            } else {
                darkBox.setChecked(false)
            }

            lightBox.setDebouncedClickListener {
                val key = colorKey ?: return@setDebouncedClickListener
                listener.onColorClicked(key, false)
            }
            lightBox.setOnLongClickListener {
                val key = colorKey ?: return@setOnLongClickListener false
                listener.onColorCleared(key, false)
                true
            }
            darkBox.setDebouncedClickListener {
                val key = colorKey ?: return@setDebouncedClickListener
                listener.onColorClicked(key, true)
            }
            darkBox.setOnLongClickListener {
                val key = colorKey ?: return@setOnLongClickListener false
                listener.onColorCleared(key, true)
                true
            }
            shuffleBtn.setDebouncedClickListener {
                val key = colorKey ?: return@setDebouncedClickListener
                listener.onShuffleClicked(key)
            }

            if (colorKey != null) {
                keyTv.visibility = View.VISIBLE
                keyTv.text = colorKey
                keyTv.alpha = if (enabled == true) 1f else KEY_DIM_ALPHA
            } else {
                keyTv.visibility = View.GONE
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ElementCustomizationSetting>() {
        override fun areItemsTheSame(oldItem: ElementCustomizationSetting, newItem: ElementCustomizationSetting) =
            oldItem.elementKey == newItem.elementKey

        override fun areContentsTheSame(oldItem: ElementCustomizationSetting, newItem: ElementCustomizationSetting) =
            oldItem == newItem
    }

    companion object {
        const val CUSTOM_FONT_SENTINEL = ElementCustomizationsVM.CUSTOM_FONT_SENTINEL
        private const val CHEVRON_ANIM_MS = 250L
        private const val BADGE_DIM_ALPHA = 0.3f
        private const val KEY_DIM_ALPHA = 0.4f

        val fontWeightValues: List<OWFontWeight?> = listOf(null) + OWFontWeight.entries
        val fontWeightLabels: List<String> = listOf("Default") + OWFontWeight.entries.map { it.toLabel() }
    }
}

private fun OWFontWeight.toLabel(): String = when (this) {
    OWFontWeight.THIN -> "Thin"
    OWFontWeight.EXTRA_LIGHT -> "ExtraLight"
    OWFontWeight.LIGHT -> "Light"
    OWFontWeight.NORMAL -> "Regular"
    OWFontWeight.MEDIUM -> "Medium"
    OWFontWeight.SEMI_BOLD -> "SemiBold"
    OWFontWeight.BOLD -> "Bold"
    OWFontWeight.EXTRA_BOLD -> "ExtraBold"
    OWFontWeight.BLACK -> "Black"
}
