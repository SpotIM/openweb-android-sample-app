package openweb.sample.ui.screens.settings.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.preference.R as PreferenceR
import openweb.sample.R
import openweb.sample.databinding.PreferenceButtonBinding

class ButtonPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = PreferenceR.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var _binding: PreferenceButtonBinding? = null
    private val binding: PreferenceButtonBinding get() = _binding!!

    init {
        layoutResource = R.layout.preference_button
        // Remove divider lines above/below
        isIconSpaceReserved = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        _binding = PreferenceButtonBinding.bind(holder.itemView)

        binding.preferenceButton.text = title
        binding.preferenceButton.setOnClickListener {
            onPreferenceClickListener?.onPreferenceClick(this)
        }
    }
}
