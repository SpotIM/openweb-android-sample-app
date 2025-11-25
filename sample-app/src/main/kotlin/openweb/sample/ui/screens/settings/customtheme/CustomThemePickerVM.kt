package openweb.sample.ui.screens.settings.customtheme

import android.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import spotIm.common.api.model.customizations.OWTheme
import spotIm.common.api.model.customizations.UIColor
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import kotlin.random.Random
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

interface CustomThemePickerVMOutputs {
    val themeColorsListFlow: StateFlow<List<CustomThemeSetting>>
    val showColorPickerDialogFlowEvent: SharedFlow<Unit>
}

interface CustomThemePickerVMInputs {
    fun onUIEvent(event: CustomThemePickerUIEvent)
}

interface CustomThemePickerVMContract {
    val inputs: CustomThemePickerVMInputs
    val outputs: CustomThemePickerVMOutputs
}

class CustomThemePickerVM(
    private val settingsRepository: SettingsRepository
) :
    ViewModel(),
    CustomThemePickerVMContract,
    CustomThemePickerVMInputs,
    CustomThemePickerVMOutputs {

    override val inputs: CustomThemePickerVMInputs = this
    override val outputs: CustomThemePickerVMOutputs = this

    private val _themeColorsListFlow = MutableStateFlow<ArrayList<CustomThemeSetting>>(arrayListOf())
    override val themeColorsListFlow: StateFlow<List<CustomThemeSetting>> = _themeColorsListFlow.asStateFlow()

    private val _showColorPickerDialogFlowEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val showColorPickerDialogFlowEvent = _showColorPickerDialogFlowEvent as SharedFlow<Unit>

    private var selectedCustomThemeItem: CustomThemePickerUIEvent.OnCustomThemeColorSelected? = null

    init {
        _themeColorsListFlow.value = getThemeColorsListFromSP()
    }

    override fun onUIEvent(event: CustomThemePickerUIEvent) {
        when (event) {
            is CustomThemePickerUIEvent.OnCustomThemeColorSelected -> handleOnColorSelected(event)
            is CustomThemePickerUIEvent.OnPickerDialogColorSelected -> handleOnPickerDialogColorSelected(event)
            is CustomThemePickerUIEvent.OnCustomThemeToggleClicked -> handleOnCustomThemeToggleClicked(event)
            is CustomThemePickerUIEvent.OnResetAllClicked -> handleOnResetAllClicked()
            is CustomThemePickerUIEvent.OnRandomColorsClicked -> handleOnRandomColorsClicked(event)
        }
    }

    /**
     * Get default theme colors from SpotImTheme class
     */
    private fun getDefaultThemeColors(): ArrayList<CustomThemeSetting> {
        val themeClass = OWTheme::class

        // Get the parameters of the primary constructor in the order they are declared
        val constructorProperties = themeClass.primaryConstructor?.parameters?.map { it.name } ?: emptyList()

        val themeProperties = themeClass.memberProperties
            .filter { it.returnType.classifier == UIColor::class }
            .sortedBy { property -> constructorProperties.indexOf(property.name) }
            .map { property ->
                property.isAccessible = true
                val title = property.name
                    .split("(?=[A-Z])".toRegex())
                    .joinToString(" ") { word ->
                        word.replace(
                            "Color",
                            ""
                        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    }
                    .trim()
                CustomThemeSetting(title)
            }

        return arrayListOf<CustomThemeSetting>().apply { addAll(themeProperties) }
    }

    private fun handleOnColorSelected(customColorThemeData: CustomThemePickerUIEvent.OnCustomThemeColorSelected) {
        selectedCustomThemeItem = customColorThemeData
        _showColorPickerDialogFlowEvent.tryEmit(Unit)
    }

    fun handleOnPickerDialogColorSelected(colorSelectedData: CustomThemePickerUIEvent.OnPickerDialogColorSelected) {
        val themeColorsList = _themeColorsListFlow.value.map { it.copy() } as ArrayList<CustomThemeSetting>

        // change selected color
        selectedCustomThemeItem?.let { selectedCustomThemeItem ->
            val themeColor = themeColorsList[selectedCustomThemeItem.position]
            val uiColor = UIColor(
                lightColor = if (!selectedCustomThemeItem.isDark) {
                    colorSelectedData.colorEnvelop.color
                } else {
                    themeColor.color?.lightColor
                },
                darkColor = if (selectedCustomThemeItem.isDark) {
                    colorSelectedData.colorEnvelop.color
                } else {
                    themeColor.color?.darkColor
                }
            )
            themeColor.color = uiColor

            // Automatically enable toggle only when both colors are set
            themeColor.toggle = uiColor.lightColor != null && uiColor.darkColor != null

            themeColorsList[selectedCustomThemeItem.position] = themeColor
            _themeColorsListFlow.value = themeColorsList

            // save to SP
            setThemeColorsListToSP(themeColorsList)
        }
    }

    fun getThemeColorsListFromSP(): ArrayList<CustomThemeSetting> {
        val customThemeColors = settingsRepository[SharedPrefsItem.CustomThemeColors]
        return customThemeColors.takeIf { !it.isNullOrEmpty() } ?: getDefaultThemeColors()
    }

    fun setThemeColorsListToSP(themeColorsList: ArrayList<CustomThemeSetting>) {
        settingsRepository[SharedPrefsItem.CustomThemeColors] = themeColorsList
    }

    fun handleOnCustomThemeToggleClicked(event: CustomThemePickerUIEvent.OnCustomThemeToggleClicked) {
        val themeColorsList = _themeColorsListFlow.value

        // change toggle state
        val themeColor = themeColorsList[event.position]
        themeColor.toggle = event.toggle
        themeColorsList[event.position] = themeColor
        _themeColorsListFlow.value = themeColorsList

        // save to SP
        setThemeColorsListToSP(themeColorsList)
    }

    fun handleOnResetAllClicked() {
        val themeColorsList = _themeColorsListFlow.value.map { it.copy() } as ArrayList<CustomThemeSetting>

        // Reset all toggles to false and clear all colors
        themeColorsList.forEachIndexed { index, themeSetting ->
            themeSetting.toggle = false
            themeSetting.color = null
            themeColorsList[index] = themeSetting
        }

        _themeColorsListFlow.update { themeColorsList }

        // save to SP
        setThemeColorsListToSP(themeColorsList)
    }

    private fun handleOnRandomColorsClicked(event: CustomThemePickerUIEvent.OnRandomColorsClicked) {
        val themeColorsList = _themeColorsListFlow.value.map { it.copy() } as ArrayList<CustomThemeSetting>

        val themeColor = themeColorsList[event.position]

        // Generate random colors for both light and dark
        val randomLightColor = generateRandomColor()
        val randomDarkColor = generateRandomColor()

        themeColor.color = UIColor(
            lightColor = randomLightColor,
            darkColor = randomDarkColor
        )

        // Automatically enable toggle since both colors are now set
        themeColor.toggle = true

        themeColorsList[event.position] = themeColor
        _themeColorsListFlow.value = themeColorsList

        // save to SP
        setThemeColorsListToSP(themeColorsList)
    }

    private fun generateRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }
}
