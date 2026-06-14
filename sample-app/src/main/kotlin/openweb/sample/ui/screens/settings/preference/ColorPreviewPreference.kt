package openweb.sample.ui.screens.settings.preference

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.preference.R as PreferenceR
import openweb.sample.R
import openweb.sample.databinding.PreferenceColorPreviewBinding
import androidx.core.graphics.toColorInt

class ColorPreviewPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = PreferenceR.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var _binding: PreferenceColorPreviewBinding? = null
    private val binding: PreferenceColorPreviewBinding get() = _binding!!

    var colorValue: Int = Color.BLACK
        set(value) {
            if (field == value) return
            field = value
            notifyChanged()
        }

    var onResetClicked: (() -> Unit)? = null

    init {
        layoutResource = R.layout.preference_color_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        _binding = PreferenceColorPreviewBinding.bind(holder.itemView)

        // Create circular colored drawable
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(colorValue)
            setStroke((2 * context.resources.displayMetrics.density).toInt(), "#CCCCCC".toColorInt())
        }

        binding.colorPreview.background = drawable

        binding.resetColorBtn.setOnClickListener { onResetClicked?.invoke() }
    }
}
