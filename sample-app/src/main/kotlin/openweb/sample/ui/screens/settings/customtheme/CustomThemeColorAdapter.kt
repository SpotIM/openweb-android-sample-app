package openweb.sample.ui.screens.settings.customtheme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import openweb.sample.databinding.ItemThemeColorBinding
import openweb.sample.utils.setDebouncedClickListener

class CustomThemeColorAdapter(
    private val onToggleClickListener: (position: Int, toggle: Boolean) -> Unit,
    private val onCheckBoxClickListener: (item: CustomThemeSetting, position: Int, isDark: Boolean) -> Unit,
    private val onRandomColorsClickListener: (position: Int) -> Unit
) : ListAdapter<CustomThemeSetting, CustomThemeColorAdapter.ThemeColorViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeColorViewHolder =
        ThemeColorViewHolder(
            ItemThemeColorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onToggleClickListener,
            onCheckBoxClickListener,
            onRandomColorsClickListener
        )

    override fun onBindViewHolder(holder: ThemeColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position).title.hashCode().toLong()

    class ThemeColorViewHolder(
        val binding: ItemThemeColorBinding,
        val onToggleClickListener: (position: Int, toggle: Boolean) -> Unit,
        val onCheckBoxClickListener: (item: CustomThemeSetting, position: Int, isDark: Boolean) -> Unit,
        val onRandomColorsClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(themeColor: CustomThemeSetting?) {
            themeColor ?: return

            binding.titleTextView.text = themeColor.title
            binding.switchToggle.isChecked = themeColor.toggle

            if (themeColor.color?.lightColor != null) {
                binding.lightCheckBox.setChecked(true, themeColor.color!!.lightColor!!)
            } else {
                binding.lightCheckBox.setChecked(false)
            }

            if (themeColor.color?.darkColor != null) {
                binding.darkCheckBox.setChecked(true, themeColor.color!!.darkColor!!)
            } else {
                binding.darkCheckBox.setChecked(false)
            }

            // Enable toggle only when both colors are set
            val bothColorsSet = themeColor.color?.lightColor != null && themeColor.color?.darkColor != null
            binding.switchToggle.isEnabled = bothColorsSet

            binding.switchToggle.setDebouncedClickListener {
                onToggleClickListener(absoluteAdapterPosition, binding.switchToggle.isChecked)
            }

            binding.lightCheckBox.setDebouncedClickListener {
                onCheckBoxClickListener(themeColor, absoluteAdapterPosition, false)
            }

            binding.darkCheckBox.setDebouncedClickListener {
                onCheckBoxClickListener(themeColor, absoluteAdapterPosition, true)
            }

            binding.randomButton.setDebouncedClickListener {
                onRandomColorsClickListener(absoluteAdapterPosition)
            }
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<CustomThemeSetting>() {
        override fun areItemsTheSame(oldItem: CustomThemeSetting, newItem: CustomThemeSetting) =
            oldItem.color == newItem.color && oldItem.toggle == newItem.toggle && oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: CustomThemeSetting, newItem: CustomThemeSetting) = oldItem == newItem
    }
}
