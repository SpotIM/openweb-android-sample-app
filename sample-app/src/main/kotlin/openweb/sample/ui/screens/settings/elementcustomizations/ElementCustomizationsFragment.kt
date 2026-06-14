package openweb.sample.ui.screens.settings.elementcustomizations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import openweb.sample.R
import openweb.sample.databinding.FragmentElementCustomizationsBinding
import openweb.sample.utils.collectLatestLifecycleFlow
import openweb.sample.utils.hideToolbarIcons
import openweb.sample.utils.setDebouncedClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import spotIm.common.api.model.customizations.OWFontWeight

/**
 * Unified "All Element Customizations" screen.
 *
 * Shows every SDK element in a single scrollable list. Each card displays all applicable
 * controls for that element: custom callback toggle, font family/weight spinners,
 * and light/dark color swatches — with non-applicable controls hidden entirely.
 */
class ElementCustomizationsFragment : Fragment(), ColorPickerDialogListener {

    private var _binding: FragmentElementCustomizationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ElementCustomizationsAdapter
    private var pendingColorRequest: ColorPickerRequest? = null

    private val viewModel: ElementCustomizationsVMContract by viewModel<ElementCustomizationsVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElementCustomizationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideToolbarIcons()
        initRecyclerView()
        setupSearch()
        setupFilterChips()
        setupClickListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = object : ElementCustomizationsListener {
            override fun onCallbackToggled(elementKey: String, enabled: Boolean) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnCallbackToggled(elementKey, enabled))

            override fun onFontFamilyChanged(elementKey: String, family: String?) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFontFamilyChanged(elementKey, family))

            override fun onFontWeightChanged(elementKey: String, weight: OWFontWeight?) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFontWeightChanged(elementKey, weight))

            override fun onColorToggled(colorKey: String, enabled: Boolean) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnColorToggled(colorKey, enabled))

            override fun onColorClicked(colorKey: String, isDark: Boolean) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnColorClicked(colorKey, isDark))

            override fun onColorCleared(colorKey: String, isDark: Boolean) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnColorCleared(colorKey, isDark))

            override fun onShuffleClicked(colorKey: String) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnShuffleClicked(colorKey))

            override fun onFontInputChanged(elementKey: String, text: String) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFontInputChanged(elementKey, text))

            override fun onEnterCustomFontMode(elementKey: String) =
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnEnterCustomFontMode(elementKey))
        }
        adapter = ElementCustomizationsAdapter(
            fontFamilyOptions = viewModel.outputs.fontFamilyOptions,
            listener = listener
        )
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.inputs.onEvent(ElementCustomizationsEvent.OnQueryChanged(newText.orEmpty()))
                return true
            }
        })
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener {
            viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFilterChanged(ElementCustomizationsFilter.ALL))
            updateChipSelection(ElementCustomizationsFilter.ALL)
        }
        binding.chipCallback.setOnClickListener {
            viewModel.inputs.onEvent(
                ElementCustomizationsEvent.OnFilterChanged(ElementCustomizationsFilter.HAS_CALLBACK)
            )
            updateChipSelection(ElementCustomizationsFilter.HAS_CALLBACK)
        }
        binding.chipFont.setOnClickListener {
            viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFilterChanged(ElementCustomizationsFilter.HAS_FONT))
            updateChipSelection(ElementCustomizationsFilter.HAS_FONT)
        }
        binding.chipColor.setOnClickListener {
            viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFilterChanged(ElementCustomizationsFilter.HAS_COLOR))
            updateChipSelection(ElementCustomizationsFilter.HAS_COLOR)
        }
        binding.chipModified.setOnClickListener {
            viewModel.inputs.onEvent(ElementCustomizationsEvent.OnFilterChanged(ElementCustomizationsFilter.MODIFIED))
            updateChipSelection(ElementCustomizationsFilter.MODIFIED)
        }
    }

    private fun updateChipSelection(selected: ElementCustomizationsFilter) {
        binding.chipAll.isChecked = selected == ElementCustomizationsFilter.ALL
        binding.chipCallback.isChecked = selected == ElementCustomizationsFilter.HAS_CALLBACK
        binding.chipFont.isChecked = selected == ElementCustomizationsFilter.HAS_FONT
        binding.chipColor.isChecked = selected == ElementCustomizationsFilter.HAS_COLOR
        binding.chipModified.isChecked = selected == ElementCustomizationsFilter.MODIFIED
    }

    private fun setupClickListeners() {
        binding.resetAllBtn.setDebouncedClickListener {
            viewModel.inputs.onEvent(ElementCustomizationsEvent.OnResetAll)
        }

        binding.expandCollapseBtn.setDebouncedClickListener {
            val btn = binding.expandCollapseBtn
            if (btn.text.toString() == getString(R.string.element_customizations_expand_all)) {
                adapter.expandAll(adapter.currentList.map { it.elementKey })
                btn.setText(R.string.element_customizations_collapse_all)
            } else {
                adapter.collapseAll()
                btn.setText(R.string.element_customizations_expand_all)
            }
        }
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.outputs.filteredListFlow) { list ->
            adapter.submitList(list)
        }

        collectLatestLifecycleFlow(viewModel.outputs.totalCount) { total ->
            updateSubtitle(total, viewModel.outputs.modifiedCount.value)
        }

        collectLatestLifecycleFlow(viewModel.outputs.modifiedCount) { modified ->
            updateSubtitle(viewModel.outputs.totalCount.value, modified)
        }

        collectLatestLifecycleFlow(viewModel.outputs.showColorPickerEvent) { request ->
            openColorPicker(request)
        }

        collectLatestLifecycleFlow(viewModel.outputs.activeFilter) { filter ->
            updateChipSelection(filter)
        }
    }

    private fun updateSubtitle(total: Int, modified: Int) {
        binding.subtitleTv.text = getString(R.string.element_customizations_subtitle, total, modified)
    }

    private fun openColorPicker(request: ColorPickerRequest) {
        pendingColorRequest = request
        val dialog = ColorPickerDialog.newBuilder()
            .setColor(request.currentColor ?: Color.WHITE)
            .setAllowPresets(false)
            .setAllowCustom(true)
            .setShowAlphaSlider(true)
            .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
            .setDialogId(COLOR_PICKER_DIALOG_ID)
            .create()
        // The library's onAttach only checks the Activity, not the parent fragment.
        // Set the listener directly so it isn't lost.
        dialog.setColorPickerDialogListener(this)
        dialog.show(childFragmentManager, "color-picker")
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        val request = pendingColorRequest ?: return
        pendingColorRequest = null
        viewModel.inputs.onEvent(
            ElementCustomizationsEvent.OnDirectColorSelected(color, request.elementKey, request.isDark)
        )
    }

    override fun onDialogDismissed(dialogId: Int) {
        pendingColorRequest = null
    }

    companion object {
        private const val COLOR_PICKER_DIALOG_ID = 1
        fun newInstance() = ElementCustomizationsFragment()
    }
}
