package openweb.sample.ui.screens.settings.customtheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import openweb.sample.R
import openweb.sample.utils.collectLatestLifecycleFlow
import openweb.sample.utils.hideToolbarIcons
import openweb.sample.utils.setDebouncedClickListener

class CustomThemePickerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomThemeColorAdapter
    private lateinit var resetButton: android.widget.Button

    private val viewModel: CustomThemePickerVMContract by viewModel<CustomThemePickerVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_custom_theme_picker, container, false)
        recyclerView = view.findViewById(R.id.colorsRV)
        resetButton = view.findViewById(R.id.resetButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbarIcons()
        initRecyclerView()
        setOnClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.outputs.themeColorsListFlow) { themeColorsList ->
            adapter.submitList(themeColorsList)
        }
        collectLatestLifecycleFlow(viewModel.outputs.showColorPickerDialogFlowEvent) {
            openColorPickerDialog { colorEnvelop ->
                viewModel.inputs.onUIEvent(CustomThemePickerUIEvent.OnPickerDialogColorSelected(colorEnvelop))
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CustomThemeColorAdapter(
            onToggleClickListener = { position, toggle ->
                viewModel.inputs.onUIEvent(CustomThemePickerUIEvent.OnCustomThemeToggleClicked(position, toggle))
            },
            onCheckBoxClickListener = { item, position, isDark ->
                viewModel.inputs.onUIEvent(CustomThemePickerUIEvent.OnCustomThemeColorSelected(item, position, isDark))
            },
            onRandomColorsClickListener = { position ->
                viewModel.inputs.onUIEvent(CustomThemePickerUIEvent.OnRandomColorsClicked(position))
            }
        )
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    private fun setOnClickListeners() {
        resetButton.setDebouncedClickListener {
            viewModel.inputs.onUIEvent(CustomThemePickerUIEvent.OnResetAllClicked)
        }
    }

    private fun openColorPickerDialog(onColorSelectedListener: (envelope: ColorEnvelope) -> Unit) {
        ColorPickerDialog.Builder(requireContext())
            .setTitle("Color Picker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(
                "Confirm",
                object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope ?: return
                        onColorSelectedListener.invoke(envelope)
                    }
                }
            )
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog?.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }

    companion object {
        fun newInstance() = CustomThemePickerFragment()
    }
}
