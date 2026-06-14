package openweb.sample.ui.screens.settings.elementcustomizations

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.screens.settings.customelement.CustomElementSetting
import openweb.sample.ui.screens.settings.customfont.CustomFontSetting
import openweb.sample.ui.screens.settings.customtheme.CombinedThemeColorSetting
import spotIm.common.api.model.customizations.OWFontWeight
import spotIm.common.api.model.customizations.UIColor
import spotIm.common.internal.model.customizations.OWElementKey
import kotlin.random.Random

enum class ElementCustomizationsFilter {
    ALL, HAS_CALLBACK, HAS_FONT, HAS_COLOR, MODIFIED
}

data class ColorPickerRequest(
    val elementKey: String,
    val isDark: Boolean,
    val currentColor: Int? = null
)

sealed class ElementCustomizationsEvent {
    data class OnCallbackToggled(val elementKey: String, val enabled: Boolean) : ElementCustomizationsEvent()
    data class OnFontFamilyChanged(val elementKey: String, val family: String?) : ElementCustomizationsEvent()
    data class OnFontWeightChanged(val elementKey: String, val weight: OWFontWeight?) : ElementCustomizationsEvent()
    data class OnColorToggled(val elementKey: String, val enabled: Boolean) : ElementCustomizationsEvent()
    data class OnColorClicked(val elementKey: String, val isDark: Boolean) : ElementCustomizationsEvent()
    data class OnShuffleClicked(val elementKey: String) : ElementCustomizationsEvent()
    data class OnDirectColorSelected(
        val color: Int,
        val elementKey: String,
        val isDark: Boolean
    ) : ElementCustomizationsEvent()
    data class OnColorCleared(val colorKey: String, val isDark: Boolean) : ElementCustomizationsEvent()
    data class OnQueryChanged(val query: String) : ElementCustomizationsEvent()
    data class OnFilterChanged(val filter: ElementCustomizationsFilter) : ElementCustomizationsEvent()
    object OnResetAll : ElementCustomizationsEvent()
    data class OnFontInputChanged(val elementKey: String, val text: String) : ElementCustomizationsEvent()
    data class OnEnterCustomFontMode(val elementKey: String) : ElementCustomizationsEvent()
}

interface ElementCustomizationsVMOutputs {
    val filteredListFlow: StateFlow<List<ElementCustomizationSetting>>
    val totalCount: StateFlow<Int>
    val modifiedCount: StateFlow<Int>
    val showColorPickerEvent: SharedFlow<ColorPickerRequest>
    val fontFamilyOptions: List<Pair<String, String?>>
    val activeFilter: StateFlow<ElementCustomizationsFilter>
}

interface ElementCustomizationsVMInputs {
    fun onEvent(event: ElementCustomizationsEvent)
}

interface ElementCustomizationsVMContract {
    val inputs: ElementCustomizationsVMInputs
    val outputs: ElementCustomizationsVMOutputs
}

class ElementCustomizationsVM(
    private val settingsRepository: SettingsRepository
) : ViewModel(),
    ElementCustomizationsVMContract,
    ElementCustomizationsVMInputs,
    ElementCustomizationsVMOutputs {

    override val inputs: ElementCustomizationsVMInputs = this
    override val outputs: ElementCustomizationsVMOutputs = this

    override val fontFamilyOptions: List<Pair<String, String?>> = buildFontOptions()

    private val fontInputBuffer = mutableMapOf<String, String>()
    private val knownFontFamilyValues = fontFamilyOptions.map { it.second }.dropLast(1)

    private val _callbacks = MutableStateFlow<List<CustomElementSetting>>(emptyList())
    private val _fonts = MutableStateFlow<List<CustomFontSetting>>(emptyList())
    private val _colors = MutableStateFlow<List<CombinedThemeColorSetting>>(emptyList())

    private val _filteredListFlow = MutableStateFlow<List<ElementCustomizationSetting>>(emptyList())
    override val filteredListFlow: StateFlow<List<ElementCustomizationSetting>> = _filteredListFlow.asStateFlow()

    private val _totalCount = MutableStateFlow(0)
    override val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    private val _modifiedCount = MutableStateFlow(0)
    override val modifiedCount: StateFlow<Int> = _modifiedCount.asStateFlow()

    private val _showColorPickerEvent = MutableSharedFlow<ColorPickerRequest>(extraBufferCapacity = 1)
    override val showColorPickerEvent: SharedFlow<ColorPickerRequest> = _showColorPickerEvent

    private val _activeFilter = MutableStateFlow(ElementCustomizationsFilter.ALL)
    override val activeFilter: StateFlow<ElementCustomizationsFilter> = _activeFilter.asStateFlow()

    private var currentQuery: String = ""

    init {
        loadAllFromPrefs()
        rebuildAndFilter()
    }

    override fun onEvent(event: ElementCustomizationsEvent) {
        when (event) {
            is ElementCustomizationsEvent.OnCallbackToggled -> handleCallbackToggled(event)
            is ElementCustomizationsEvent.OnFontFamilyChanged -> handleFontFamilyChanged(event)
            is ElementCustomizationsEvent.OnFontWeightChanged -> handleFontWeightChanged(event)
            is ElementCustomizationsEvent.OnColorToggled -> handleColorToggled(event)
            is ElementCustomizationsEvent.OnColorClicked -> handleColorClicked(event)
            is ElementCustomizationsEvent.OnDirectColorSelected ->
                applyColorSelection(event.color, event.elementKey, event.isDark)
            is ElementCustomizationsEvent.OnShuffleClicked -> handleShuffleClicked(event)
            is ElementCustomizationsEvent.OnColorCleared -> handleColorCleared(event)
            is ElementCustomizationsEvent.OnQueryChanged -> handleQueryChanged(event)
            is ElementCustomizationsEvent.OnFilterChanged -> handleFilterChanged(event)
            is ElementCustomizationsEvent.OnResetAll -> handleResetAll()
            is ElementCustomizationsEvent.OnFontInputChanged -> fontInputBuffer[event.elementKey] = event.text
            is ElementCustomizationsEvent.OnEnterCustomFontMode -> {
                fontInputBuffer[event.elementKey] = ""
                rebuildAndFilter()
            }
        }
    }

    private fun handleCallbackToggled(event: ElementCustomizationsEvent.OnCallbackToggled) {
        val updated = _callbacks.value.map { setting ->
            if (setting.elementKey == event.elementKey) setting.copy(isEnabled = event.enabled) else setting
        }
        _callbacks.value = updated
        settingsRepository[SharedPrefsItem.CustomElementToggles] = updated
        rebuildAndFilter()
    }

    private fun handleFontFamilyChanged(event: ElementCustomizationsEvent.OnFontFamilyChanged) {
        fontInputBuffer.remove(event.elementKey)
        val updated = _fonts.value.map { setting ->
            if (setting.elementKey == event.elementKey) setting.copy(fontFamily = event.family) else setting
        }
        _fonts.value = updated
        settingsRepository[SharedPrefsItem.CustomFontElements] = updated
        rebuildAndFilter()
    }

    private fun handleFontWeightChanged(event: ElementCustomizationsEvent.OnFontWeightChanged) {
        val updated = _fonts.value.map { setting ->
            if (setting.elementKey == event.elementKey) setting.copy(fontWeight = event.weight?.weight) else setting
        }
        _fonts.value = updated
        settingsRepository[SharedPrefsItem.CustomFontElements] = updated
        rebuildAndFilter()
    }

    private fun handleColorToggled(event: ElementCustomizationsEvent.OnColorToggled) {
        val (colorIndex, colorUsesLegacy) = findColorEntry(event.elementKey) ?: return
        updateColor(colorIndex, colorUsesLegacy) { item ->
            if (colorUsesLegacy) {
                item.copy(legacyToggle = event.enabled)
            } else {
                // Only update elementToggle for new API; do NOT cross-update legacy
                item.copy(elementToggle = event.enabled)
            }
        }
    }

    private fun handleColorClicked(event: ElementCustomizationsEvent.OnColorClicked) {
        val currentColor = findColorEntry(event.elementKey)?.let { (colorIndex, usesLegacy) ->
            val setting = _colors.value[colorIndex]
            if (usesLegacy) {
                if (event.isDark) setting.legacyColor?.darkColor else setting.legacyColor?.lightColor
            } else {
                if (event.isDark) setting.elementColor?.darkColor else setting.elementColor?.lightColor
            }
        }
        viewModelScope.launch {
            _showColorPickerEvent.emit(ColorPickerRequest(event.elementKey, event.isDark, currentColor))
        }
    }


    private fun applyColorSelection(color: Int, elementKey: String, isDark: Boolean) {
        val (colorIndex, colorUsesLegacy) = findColorEntry(elementKey) ?: return
        updateColor(colorIndex, colorUsesLegacy) { item ->
            if (colorUsesLegacy) {
                val newLight = if (!isDark) color else item.legacyColor?.lightColor
                val newDark = if (isDark) color else item.legacyColor?.darkColor
                val newColor = UIColor(lightColor = newLight, darkColor = newDark)
                item.copy(
                    legacyColor = newColor,
                    legacyToggle = newLight != null && newDark != null
                )
            } else {
                val newElementLight = if (!isDark) color else item.elementColor?.lightColor
                val newElementDark = if (isDark) color else item.elementColor?.darkColor
                val newElementColor = UIColor(lightColor = newElementLight, darkColor = newElementDark)
                item.copy(
                    elementColor = newElementColor,
                    elementToggle = newElementLight != null && newElementDark != null
                )
            }
        }
    }

    private fun handleShuffleClicked(event: ElementCustomizationsEvent.OnShuffleClicked) {
        val (colorIndex, colorUsesLegacy) = findColorEntry(event.elementKey) ?: return
        val newColor = UIColor(lightColor = generateRandomColor(), darkColor = generateRandomColor())
        updateColor(colorIndex, colorUsesLegacy) { item ->
            if (colorUsesLegacy) {
                item.copy(legacyColor = newColor, legacyToggle = true)
            } else {
                // Only update new API; do NOT cross-update legacy
                item.copy(elementColor = newColor, elementToggle = true)
            }
        }
    }

    private fun handleColorCleared(event: ElementCustomizationsEvent.OnColorCleared) {
        val (colorIndex, colorUsesLegacy) = findColorEntry(event.colorKey) ?: return
        updateColor(colorIndex, colorUsesLegacy) { item ->
            if (colorUsesLegacy) {
                val newLight = if (!event.isDark) null else item.legacyColor?.lightColor
                val newDark = if (event.isDark) null else item.legacyColor?.darkColor
                val newColor = if (newLight != null || newDark != null) UIColor(newLight, newDark) else null
                item.copy(legacyColor = newColor, legacyToggle = newLight != null && newDark != null)
            } else {
                val newLight = if (!event.isDark) null else item.elementColor?.lightColor
                val newDark = if (event.isDark) null else item.elementColor?.darkColor
                val newColor = if (newLight != null || newDark != null) UIColor(newLight, newDark) else null
                item.copy(elementColor = newColor, elementToggle = newLight != null && newDark != null)
            }
        }
    }

    private fun handleQueryChanged(event: ElementCustomizationsEvent.OnQueryChanged) {
        currentQuery = event.query
        rebuildAndFilter()
    }

    private fun handleFilterChanged(event: ElementCustomizationsEvent.OnFilterChanged) {
        _activeFilter.value = event.filter
        rebuildAndFilter()
    }

    private fun handleResetAll() {
        fontInputBuffer.clear()
        val resetCallbacks = _callbacks.value.map { it.copy(isEnabled = false) }
        val resetFonts = _fonts.value.map { it.copy(fontFamily = null, fontWeight = null) }
        val resetColors = _colors.value.map {
            it.copy(legacyColor = null, legacyToggle = false, elementColor = null, elementToggle = false)
        }
        _callbacks.value = resetCallbacks
        _fonts.value = resetFonts
        _colors.value = resetColors
        settingsRepository[SharedPrefsItem.CustomElementToggles] = resetCallbacks
        settingsRepository[SharedPrefsItem.CustomFontElements] = resetFonts
        settingsRepository[SharedPrefsItem.CombinedThemeColors] = ArrayList(resetColors)
        rebuildAndFilter()
    }

    private fun updateColor(
        colorIndex: Int?,
        @Suppress("UNUSED_PARAMETER") colorUsesLegacy: Boolean,
        transform: (CombinedThemeColorSetting) -> CombinedThemeColorSetting
    ) {
        if (colorIndex == null) return
        val updated = _colors.value.toMutableList()
        updated[colorIndex] = transform(updated[colorIndex])
        _colors.value = updated
        settingsRepository[SharedPrefsItem.CombinedThemeColors] = ArrayList(updated)
        rebuildAndFilter()
    }

    private fun findColorEntry(elementKey: String): Pair<Int, Boolean>? {
        _colors.value.forEachIndexed { index, setting ->
            if (setting.elementKey == elementKey) return Pair(index, false)
            if (setting.legacyKey == elementKey) return Pair(index, true)
        }
        return null
    }

    private fun loadAllFromPrefs() {
        val persistedCallbacks = settingsRepository[SharedPrefsItem.CustomElementToggles]
        _callbacks.value = if (!persistedCallbacks.isNullOrEmpty()) {
            DEFAULT_CALLBACK_ELEMENTS.map { default ->
                persistedCallbacks.find { it.elementKey == default.elementKey } ?: default
            }
        } else {
            DEFAULT_CALLBACK_ELEMENTS
        }

        val persistedFonts = settingsRepository[SharedPrefsItem.CustomFontElements]
        _fonts.value = if (!persistedFonts.isNullOrEmpty()) {
            DEFAULT_FONT_ELEMENTS.map { default ->
                persistedFonts.find { it.elementKey == default.elementKey } ?: default
            }
        } else {
            DEFAULT_FONT_ELEMENTS
        }

        val defaults = getDefaultColorList()
        val storedColors = settingsRepository[SharedPrefsItem.CombinedThemeColors]
        _colors.value = if (!storedColors.isNullOrEmpty()) {
            val storedByTitle = storedColors.associateBy { it.title }
            defaults.map { default ->
                val saved = storedByTitle[default.title] ?: return@map default
                default.copy(
                    legacyColor = saved.legacyColor,
                    legacyToggle = saved.legacyToggle,
                    elementColor = saved.elementColor,
                    elementToggle = saved.elementToggle
                )
            }
        } else {
            defaults
        }
    }

    internal fun buildUnifiedList(): List<ElementCustomizationSetting> {
        val callbacksByKey = _callbacks.value.associateBy { it.elementKey }
        val fontsByKey = _fonts.value.associateBy { it.elementKey }
        val colorsByElementKey = mutableMapOf<String, Pair<Int, CombinedThemeColorSetting>>()
        val colorsByLegacyKey = mutableMapOf<String, Pair<Int, CombinedThemeColorSetting>>()

        _colors.value.forEachIndexed { index, setting ->
            setting.elementKey?.let { colorsByElementKey[it] = Pair(index, setting) }
            setting.legacyKey?.let { colorsByLegacyKey[it] = Pair(index, setting) }
        }

        val result = mutableListOf<ElementCustomizationSetting>()
        val processedColorIndices = mutableSetOf<Int>()

        for (key in MASTER_ELEMENT_ORDER) {
            val callback = callbacksByKey[key]
            val font = fontsByKey[key]

            // Check both modern and legacy independently
            val modernEntry = colorsByElementKey[key]
            val legacyEntry = colorsByLegacyKey[key]

            // Prefer modern entry for colorIndex tracking; fall back to legacy
            val primaryEntry = modernEntry ?: legacyEntry
            val colorIndex = primaryEntry?.first
            val colorSetting = primaryEntry?.second

            if (colorIndex != null) processedColorIndices.add(colorIndex)

            val displayName = callback?.displayName
                ?: font?.displayName
                ?: colorSetting?.title
                ?: key

            // Build new API color data (from elementKey match)
            val colorNewEnabled: Boolean?
            val colorNewLight: Int?
            val colorNewDark: Int?
            val modernColorKey: String?

            if (modernEntry != null) {
                val ms = modernEntry.second
                colorNewEnabled = ms.elementToggle
                colorNewLight = ms.elementColor?.lightColor
                colorNewDark = ms.elementColor?.darkColor
                modernColorKey = ms.elementKey
            } else {
                colorNewEnabled = null
                colorNewLight = null
                colorNewDark = null
                modernColorKey = null
            }

            // Build legacy API color data — from the same setting if it has a legacyKey,
            // OR from a separate legacy-keyed entry for this MASTER_ELEMENT_ORDER key
            val resolvedLegacyEntry = when {
                modernEntry != null && modernEntry.second.legacyKey != null -> modernEntry
                legacyEntry != null -> legacyEntry
                else -> null
            }

            val colorLegacyEnabled: Boolean?
            val colorLegacyLight: Int?
            val colorLegacyDark: Int?
            val legacyColorKey: String?

            if (resolvedLegacyEntry != null) {
                val ls = resolvedLegacyEntry.second
                colorLegacyEnabled = ls.legacyToggle
                colorLegacyLight = ls.legacyColor?.lightColor
                colorLegacyDark = ls.legacyColor?.darkColor
                legacyColorKey = ls.legacyKey
            } else {
                colorLegacyEnabled = null
                colorLegacyLight = null
                colorLegacyDark = null
                legacyColorKey = null
            }

            // Only add if this key is relevant (has at least one control)
            if (callback != null || font != null || colorSetting != null) {
                // Autopopulate buffer for previously-saved custom font families (e.g. after app restore)
                val fontFamily = font?.fontFamily
                if (fontFamily != null && fontFamily !in knownFontFamilyValues && key !in fontInputBuffer) {
                    fontInputBuffer[key] = fontFamily
                }

                result.add(
                    ElementCustomizationSetting(
                        elementKey = key,
                        displayName = displayName,
                        callbackEnabled = callback?.isEnabled,
                        hasFontControl = font != null,
                        fontFamily = font?.fontFamily,
                        fontWeight = OWFontWeight.fromWeight(font?.fontWeight),
                        colorNewEnabled = colorNewEnabled,
                        colorNewLight = colorNewLight,
                        colorNewDark = colorNewDark,
                        colorLegacyEnabled = colorLegacyEnabled,
                        colorLegacyLight = colorLegacyLight,
                        colorLegacyDark = colorLegacyDark,
                        legacyColorKey = legacyColorKey,
                        modernColorKey = modernColorKey,
                        colorIndex = colorIndex,
                        customFontInput = if (font != null) fontInputBuffer[key] else null
                    )
                )
            }
        }

        // Add legacy-only entries (colors where elementKey == null) not already processed
        _colors.value.forEachIndexed { index, setting ->
            if (setting.elementKey == null && setting.legacyKey != null && index !in processedColorIndices) {
                processedColorIndices.add(index)
                result.add(
                    ElementCustomizationSetting(
                        elementKey = setting.legacyKey,
                        displayName = setting.title,
                        callbackEnabled = null,
                        hasFontControl = false,
                        colorNewEnabled = null,
                        colorNewLight = null,
                        colorNewDark = null,
                        colorLegacyEnabled = setting.legacyToggle,
                        colorLegacyLight = setting.legacyColor?.lightColor,
                        colorLegacyDark = setting.legacyColor?.darkColor,
                        legacyColorKey = setting.legacyKey,
                        modernColorKey = null,
                        colorIndex = index
                    )
                )
            }
        }

        return result
    }

    private fun applyFilterAndSearch(list: List<ElementCustomizationSetting>): List<ElementCustomizationSetting> {
        val filtered = when (_activeFilter.value) {
            ElementCustomizationsFilter.ALL -> list
            ElementCustomizationsFilter.HAS_CALLBACK -> list.filter { it.hasCallback }
            ElementCustomizationsFilter.HAS_FONT -> list.filter { it.hasFontControl }
            ElementCustomizationsFilter.HAS_COLOR -> list.filter { it.hasColorSetting }
            ElementCustomizationsFilter.MODIFIED -> list.filter { it.isModified }
        }

        val query = currentQuery.trim()
        return if (query.isEmpty()) {
            filtered
        } else {
            filtered.filter { item ->
                item.displayName.contains(query, ignoreCase = true) ||
                    item.elementKey.contains(query, ignoreCase = true) ||
                    (item.legacyColorKey?.contains(query, ignoreCase = true) == true) ||
                    (item.modernColorKey?.contains(query, ignoreCase = true) == true)
            }
        }
    }

    internal fun rebuildAndFilter() {
        val full = buildUnifiedList()
        _totalCount.value = full.size
        _modifiedCount.value = full.count { it.isModified }
        _filteredListFlow.value = applyFilterAndSearch(full)
    }

    private fun generateRandomColor(): Int =
        Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

    @Suppress("MaxLineLength", "StringLiteralDuplication")
    private fun getDefaultColorList(): List<CombinedThemeColorSetting> = listOf(
        // Rows with both legacy and new-API mappings
        CombinedThemeColorSetting("Brand Color", legacyKey = "brandColor", elementKey = OWElementKey.BRAND.key),
        CombinedThemeColorSetting("Comment Body", legacyKey = "primaryTextColor", elementKey = OWElementKey.COMMENT_BODY.key),
        CombinedThemeColorSetting("Subtitle", legacyKey = "secondaryTextColor", elementKey = OWElementKey.SUBTITLE.key),
        CombinedThemeColorSetting("Detail", legacyKey = "tertiaryTextColor", elementKey = OWElementKey.DETAIL.key),
        CombinedThemeColorSetting("Background", legacyKey = "primaryBackgroundColor", elementKey = OWElementKey.BACKGROUND.key),
        CombinedThemeColorSetting("Overlay Background", legacyKey = "secondaryBackgroundColor", elementKey = OWElementKey.OVERLAY_BACKGROUND.key),
        CombinedThemeColorSetting("Card Background", legacyKey = "tertiaryBackgroundColor", elementKey = OWElementKey.CARD_BACKGROUND.key),
        CombinedThemeColorSetting("Border", legacyKey = "primaryBorderColor", elementKey = OWElementKey.BORDER.key),
        CombinedThemeColorSetting("Section Divider", legacyKey = "primarySeparatorColor", elementKey = OWElementKey.SECTION_DIVIDER.key),
        CombinedThemeColorSetting("Content Divider", legacyKey = "secondarySeparatorColor", elementKey = OWElementKey.CONTENT_DIVIDER.key),
        CombinedThemeColorSetting("Divider", legacyKey = "tertiarySeparatorColor", elementKey = OWElementKey.DIVIDER.key),
        CombinedThemeColorSetting("Loader", legacyKey = "loaderColor", elementKey = OWElementKey.LOADER.key),
        CombinedThemeColorSetting("Skeleton Edge", legacyKey = "skeletonColor", elementKey = OWElementKey.SKELETON_GRADIENT_EDGE.key),
        CombinedThemeColorSetting("Skeleton Center", legacyKey = "skeletonShimmeringColor", elementKey = OWElementKey.SKELETON_GRADIENT_CENTER.key),
        CombinedThemeColorSetting("Vote Up Selected", legacyKey = "voteUpSelectedColor", elementKey = OWElementKey.VOTE_UP_SELECTED.key),
        CombinedThemeColorSetting("Vote Down Selected", legacyKey = "voteDownSelectedColor", elementKey = OWElementKey.VOTE_DOWN_SELECTED.key),
        CombinedThemeColorSetting("Vote Up Unselected", legacyKey = "voteUpUnselectedColor", elementKey = OWElementKey.VOTE_UP_UNSELECTED.key),
        CombinedThemeColorSetting("Vote Down Unselected", legacyKey = "voteDownUnselectedColor", elementKey = OWElementKey.VOTE_DOWN_UNSELECTED.key),
        // Legacy-only rows
        CombinedThemeColorSetting("Status Bar Color", legacyKey = "statusBarColor", elementKey = null),
        CombinedThemeColorSetting("Navigation Bar Color", legacyKey = "navigationBarColor", elementKey = null),
        CombinedThemeColorSetting("Social Review Summary", legacyKey = "socialReviewSummaryColor", elementKey = null),
        // New-API-only rows
        CombinedThemeColorSetting("Navigation Title", legacyKey = null, elementKey = OWElementKey.NAVIGATION_TITLE.key),
        CombinedThemeColorSetting("Commenter Name", legacyKey = null, elementKey = OWElementKey.COMMENTER_NAME.key),
        CombinedThemeColorSetting("Input Text", legacyKey = null, elementKey = OWElementKey.INPUT_TEXT.key),
        CombinedThemeColorSetting("Comment Actions", legacyKey = null, elementKey = OWElementKey.COMMENT_ACTIONS.key),
        CombinedThemeColorSetting("Avatar Text", legacyKey = null, elementKey = OWElementKey.AVATAR_TEXT.key),
    )

    companion object {

        const val CUSTOM_FONT_SENTINEL = "__custom__"

        @Suppress("MaxLineLength")
        val MASTER_ELEMENT_ORDER = listOf(
            // Elements with callback + font + color (or subsets)
            OWElementKey.NAVIGATION_TITLE.key, OWElementKey.COMMENTER_NAME.key,
            // Elements with font + color (no callback)
            OWElementKey.COMMENT_BODY.key, OWElementKey.COMMENT_ACTIONS.key, OWElementKey.INPUT_TEXT.key, OWElementKey.AVATAR_TEXT.key,
            // Callback-only text elements
            OWElementKey.LOGIN_PROMPT.key, OWElementKey.COMMUNITY_QUESTION.key, OWElementKey.COMMUNITY_GUIDELINES.key,
            OWElementKey.SAY_CONTROL_PRE_CONVERSATION.key, OWElementKey.SAY_CONTROL_CONVERSATION.key,
            OWElementKey.PRE_CONVERSATION_HEADER_TEXT.key, OWElementKey.PRE_CONVERSATION_HEADER_COUNTER.key, OWElementKey.PRE_CONVERSATION_HEADER_USER_COUNT.key,
            OWElementKey.READ_ONLY_TEXT.key, OWElementKey.EMPTY_STATE_READ_ONLY_TEXT.key,
            OWElementKey.CONVERSATION_COMMENT_COUNT.key, OWElementKey.CONVERSATION_USER_COUNT.key, OWElementKey.CONVERSATION_SORT_TEXT.key, OWElementKey.CONVERSATION_SORT_DROPDOWN_ITEM.key,
            // Callback-only view elements
            OWElementKey.NAVIGATION_BACK_ICON.key, OWElementKey.NAVIGATION_TOOLBAR.key, OWElementKey.SHOW_COMMENTS_BUTTON.key,
            OWElementKey.COMMENT_CREATION_ACTION_BUTTON.key, OWElementKey.COMMENT_CREATION_ACTION_IMAGE_BUTTON.key,
            OWElementKey.CONVERSATION_FOOTER.key, OWElementKey.CONVERSATION_INFO_LAYOUT.key, OWElementKey.CONVERSATION_SORT_SPINNER.key,
            // Color-only elements (modern API)
            OWElementKey.BRAND.key, OWElementKey.SUBTITLE.key, OWElementKey.DETAIL.key, OWElementKey.BACKGROUND.key, OWElementKey.OVERLAY_BACKGROUND.key, OWElementKey.CARD_BACKGROUND.key,
            OWElementKey.BORDER.key, OWElementKey.SECTION_DIVIDER.key, OWElementKey.CONTENT_DIVIDER.key, OWElementKey.DIVIDER.key,
            OWElementKey.LOADER.key, OWElementKey.SKELETON_GRADIENT_EDGE.key, OWElementKey.SKELETON_GRADIENT_CENTER.key,
            OWElementKey.VOTE_UP_SELECTED.key, OWElementKey.VOTE_DOWN_SELECTED.key, OWElementKey.VOTE_UP_UNSELECTED.key, OWElementKey.VOTE_DOWN_UNSELECTED.key
        )

        val DEFAULT_CALLBACK_ELEMENTS = listOf(
            // Text elements
            CustomElementSetting(OWElementKey.NAVIGATION_TITLE.key, "Navigation Title"),
            CustomElementSetting(OWElementKey.LOGIN_PROMPT.key, "Login Prompt"),
            CustomElementSetting(OWElementKey.COMMUNITY_QUESTION.key, "Community Question"),
            CustomElementSetting(OWElementKey.COMMUNITY_GUIDELINES.key, "Community Guidelines"),
            CustomElementSetting(OWElementKey.SAY_CONTROL_PRE_CONVERSATION.key, "Say Control (Pre-Conv)"),
            CustomElementSetting(OWElementKey.SAY_CONTROL_CONVERSATION.key, "Say Control (Conv)"),
            CustomElementSetting(OWElementKey.PRE_CONVERSATION_HEADER_TEXT.key, "Pre-Conv Header Text"),
            CustomElementSetting(OWElementKey.PRE_CONVERSATION_HEADER_COUNTER.key, "Pre-Conv Header Counter"),
            CustomElementSetting(
                OWElementKey.PRE_CONVERSATION_HEADER_USER_COUNT.key, "Pre-Conv Header User Count"
            ),
            CustomElementSetting(OWElementKey.COMMENTER_NAME.key, "Commenter Name"),
            CustomElementSetting(OWElementKey.READ_ONLY_TEXT.key, "Read-Only Text"),
            CustomElementSetting(OWElementKey.EMPTY_STATE_READ_ONLY_TEXT.key, "Empty State Read-Only Text"),
            CustomElementSetting(OWElementKey.CONVERSATION_COMMENT_COUNT.key, "Comment Count"),
            CustomElementSetting(OWElementKey.CONVERSATION_USER_COUNT.key, "User Count"),
            CustomElementSetting(OWElementKey.CONVERSATION_SORT_TEXT.key, "Sort Label"),
            CustomElementSetting(OWElementKey.CONVERSATION_SORT_DROPDOWN_ITEM.key, "Sort Dropdown Item"),
            // View elements
            CustomElementSetting(OWElementKey.NAVIGATION_BACK_ICON.key, "Navigation Back Icon"),
            CustomElementSetting(OWElementKey.NAVIGATION_TOOLBAR.key, "Navigation Toolbar"),
            CustomElementSetting(OWElementKey.SHOW_COMMENTS_BUTTON.key, "Show Comments Button"),
            CustomElementSetting(OWElementKey.COMMENT_CREATION_ACTION_BUTTON.key, "Comment Submit Button"),
            CustomElementSetting(
                OWElementKey.COMMENT_CREATION_ACTION_IMAGE_BUTTON.key, "Comment Submit Image Button"
            ),
            CustomElementSetting(OWElementKey.CONVERSATION_FOOTER.key, "Conversation Footer"),
            CustomElementSetting(OWElementKey.CONVERSATION_INFO_LAYOUT.key, "Conversation Info Layout"),
            CustomElementSetting(OWElementKey.CONVERSATION_SORT_SPINNER.key, "Sort Spinner"),
        )

        val DEFAULT_FONT_ELEMENTS = listOf(
            CustomFontSetting(elementKey = OWElementKey.COMMENT_ACTIONS.key, displayName = "Comment Actions"),
            CustomFontSetting(elementKey = OWElementKey.NAVIGATION_TITLE.key, displayName = "Navigation Title"),
            CustomFontSetting(elementKey = OWElementKey.COMMENTER_NAME.key, displayName = "Commenter Name"),
            CustomFontSetting(elementKey = OWElementKey.COMMENT_BODY.key, displayName = "Comment Body"),
            CustomFontSetting(elementKey = OWElementKey.INPUT_TEXT.key, displayName = "Input Text"),
            CustomFontSetting(elementKey = OWElementKey.AVATAR_TEXT.key, displayName = "Avatar Text"),
        )

        /**
         * Builds a short list of font family options including a custom-entry sentinel.
         */
        fun buildFontOptions(): List<Pair<String, String?>> = listOf(
            "Default" to null,
            "The Times" to "the_times_custom_font_family",
            "Tourney Condensed" to "tourney_condensed_font_family",
            "Custom\u2026" to CUSTOM_FONT_SENTINEL
        )
    }
}
