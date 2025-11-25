package openweb.sample.ui.screens.settings

import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import openweb.sample.data.local.SharedPrefsItem
import openweb.sample.data.repository.SettingsRepository
import openweb.sample.ui.features.floatinglogger.FloatingLoggerManager
import openweb.sample.ui.screens.settings.customtheme.CustomDarkColorState
import openweb.sample.ui.screens.settings.enums.ArticleInformationStrategy
import spotIm.common.api.model.localization.OWLanguageStrategy
import spotIm.common.api.model.settings.conversation.style.OWConversationSpacing
import spotIm.common.internal.model.settings.OWEnvironment

interface SettingsVMInputs {
    fun clearConfigButtonClicked()
    fun onResetSettingsClicked()

    // Environment
    fun onEnvironmentChanged(environment: String)
    fun onApplyEnvironmentClicked(selectedEnv: String, baseUrl: String?)
    fun validateUrl(url: String): Pair<Boolean, String?>
    fun validateSpacing(value: String): String?

    // Color
    fun onCustomDarkColorClicked()
    fun onCustomDarkColorSelected(color: Int)
    fun onCustomDarkColorReset()

    // Logger
    fun onLoggerViewChanged(enabled: Boolean)

    // Language
    fun onLanguageStrategyChanged(strategy: String)

    // Article
    fun onArticleInformationStrategyChanged(strategy: String)

    // PreConversation
    fun onPreConversationStyleChanged(style: String)

    // Conversation
    fun onConversationStyleChanged(style: String)
    fun onConversationSpacingStyleChanged(style: String)
}

interface SettingsVMOutputs {
    val events: SharedFlow<SettingsEvent>
    val enabledState: StateFlow<PreferenceEnabledState>
    val customDarkColorState: StateFlow<CustomDarkColorState>
}

interface SettingsVMContract {
    val inputs: SettingsVMInputs
    val outputs: SettingsVMOutputs
}

/**
 * ViewModel managing settings screen business logic and UI state.
 *
 * Handles:
 * - Preference value changes and validation
 * - Environment switching with app restart
 * - Custom theme color selection
 * - Logger management
 * - Dynamic preference enabling/disabling based on settings
 *
 * Follows input/output pattern to separate concerns between Fragment and ViewModel.
 * Fragment observes [outputs] and calls [inputs] methods.
 */
@Suppress("StringLiteralDuplication")
class SettingsVM(
    private val floatingLoggerManager: FloatingLoggerManager,
    private val settingsRepository: SettingsRepository
) : ViewModel(), SettingsVMContract, SettingsVMInputs, SettingsVMOutputs {

    override val inputs: SettingsVMInputs = this
    override val outputs: SettingsVMOutputs = this

    private val _events = MutableSharedFlow<SettingsEvent>(extraBufferCapacity = 10)
    override val events = _events.asSharedFlow()

    private val _enabledState = MutableStateFlow(PreferenceEnabledState())
    override val enabledState: StateFlow<PreferenceEnabledState> = _enabledState.asStateFlow()

    private val _customDarkColorState = MutableStateFlow(CustomDarkColorState())
    override val customDarkColorState: StateFlow<CustomDarkColorState> = _customDarkColorState.asStateFlow()

    init {
        // Initialize state from repository
        updateEnabledStateFromRepository()
        updateCustomDarkColorState()
    }

    private fun updateEnabledStateFromRepository() {
        val currentEnv = settingsRepository[SharedPrefsItem.Environment]
        val currentLanguageStrategy = settingsRepository[SharedPrefsItem.LanguageStrategy]
        val currentArticleStrategy = settingsRepository[SharedPrefsItem.ArticleInformationStrategy]
        val currentPreConvStyle = settingsRepository[SharedPrefsItem.PreConversationStyle]
        val currentConvStyle = settingsRepository[SharedPrefsItem.ConversationStyle]

        _enabledState.value = PreferenceEnabledState(
            environmentCustomFieldsEnabled = currentEnv == OWEnvironment.Custom,
            applyButtonEnabled = false,
            customLanguageEnabled = currentLanguageStrategy is OWLanguageStrategy.Custom,
            articleAssociatedUrlEnabled = currentArticleStrategy == ArticleInformationStrategy.Local,
            preConversationCustomOptionsEnabled = currentPreConvStyle.style ==
                openweb.sample.ui.model.PreConversationStyle.Custom,
            conversationCustomOptionsEnabled = currentConvStyle.style ==
                openweb.sample.ui.model.ConversationStyle.Custom,
            conversationSpacingCustomFieldsEnabled = currentConvStyle.style ==
                openweb.sample.ui.model.ConversationStyle.Custom &&
                currentConvStyle.conversationSpacingStyle == openweb.sample.ui.model.ConversationStyle.Custom
        )
    }

    private fun updateCustomDarkColorState() {
        val currentColor = settingsRepository[SharedPrefsItem.CustomDarkColor].takeIf { it != -1 }
            ?: "#070707".toColorInt()
        val hexColor = String.format("#%06X", 0xFFFFFF and currentColor)
        _customDarkColorState.value = CustomDarkColorState(
            colorValue = currentColor,
            hexString = hexColor
        )
    }

    override fun clearConfigButtonClicked() {
        clearConfig()
    }

    override fun onResetSettingsClicked() {
        _events.tryEmit(
            SettingsEvent.ShowConfirmationDialog(
                title = "Reset All Settings",
                message = "This will reset all settings to their default values. Are you sure?",
                onConfirm = {
                    val keysToPreserve = listOf(
                        SharedPrefsItem.SpotId.key,
                        SharedPrefsItem.PostId.key
                    )

                    val needsRestart = settingsRepository.clearAllSettings(keysToPreserve)

                    if (needsRestart) {
                        _events.tryEmit(SettingsEvent.ResetSettings)
                        _events.tryEmit(SettingsEvent.TriggerRestart)
                    } else {
                        _events.tryEmit(SettingsEvent.ResetSettings)
                    }
                }
            )
        )
    }

    override fun onEnvironmentChanged(environment: String) {
        val isCustom = environment == OWEnvironment.Custom.name
        val persistedEnv = settingsRepository[SharedPrefsItem.Environment]
        val applyEnabled = runCatching {
            OWEnvironment.valueOf(environment) != persistedEnv
        }.getOrDefault(true)

        _enabledState.value = _enabledState.value.copy(
            environmentCustomFieldsEnabled = isCustom,
            applyButtonEnabled = applyEnabled
        )
    }

    override fun onApplyEnvironmentClicked(selectedEnv: String, baseUrl: String?) {
        val newEnvironment = runCatching {
            OWEnvironment.valueOf(selectedEnv)
        }.getOrElse {
            _events.tryEmit(SettingsEvent.ShowToast("Invalid environment selected"))
            return
        }

        // Validate URL for Custom environment
        if (newEnvironment == OWEnvironment.Custom) {
            val url = baseUrl ?: settingsRepository[SharedPrefsItem.EnvironmentBaseUrl]
            val (isValid, errorMessage) = validateUrl(url ?: "")
            if (!isValid) {
                _events.tryEmit(SettingsEvent.ShowToast(errorMessage ?: "Invalid URL"))
                return
            }
        }

        // Show confirmation dialog
        val url = if (newEnvironment == OWEnvironment.Custom) {
            baseUrl ?: settingsRepository[SharedPrefsItem.EnvironmentBaseUrl]
        } else {
            null
        }

        val message = if (url != null) {
            "Apply custom environment with URL:\n$url\n\nThis will restart the app. Continue?"
        } else {
            "Apply ${newEnvironment.name} environment?\n\nThis will restart the app."
        }

        _events.tryEmit(
            SettingsEvent.ShowConfirmationDialog(
                title = "Apply Environment",
                message = message,
                onConfirm = {
                    settingsRepository[SharedPrefsItem.Environment] = newEnvironment
                    _events.tryEmit(SettingsEvent.ChangeEnvironment(newEnvironment, url))
                    _events.tryEmit(SettingsEvent.TriggerRestart)
                }
            )
        )
    }

    override fun validateUrl(url: String): Pair<Boolean, String?> =
        when {
            url.isEmpty() -> Pair(false, "Please enter a base URL first")
            !url.startsWith("http://") && !url.startsWith("https://") ->
                Pair(false, "URL must start with http:// or https://")

            else -> Pair(true, null)
        }

    override fun validateSpacing(value: String): String? =
        when {
            value.isEmpty() -> null
            value.toIntOrNull() == null -> "Must be a valid number"
            value.toInt() < OWConversationSpacing.Metrics.MIN_SPACE ->
                "Minimum value is ${OWConversationSpacing.Metrics.MIN_SPACE}"

            value.toInt() > OWConversationSpacing.Metrics.MAX_SPACE ->
                "Maximum value is ${OWConversationSpacing.Metrics.MAX_SPACE}"

            else -> null
        }

    override fun onCustomDarkColorClicked() {
        _events.tryEmit(SettingsEvent.ShowColorPicker(_customDarkColorState.value.colorValue))
    }

    override fun onCustomDarkColorSelected(color: Int) {
        settingsRepository[SharedPrefsItem.CustomDarkColor] = color
        updateCustomDarkColorState()
    }

    override fun onCustomDarkColorReset() {
        settingsRepository[SharedPrefsItem.CustomDarkColor] = -1
        updateCustomDarkColorState()
    }

    override fun onLoggerViewChanged(enabled: Boolean) {
        if (enabled) {
            floatingLoggerManager.enableFloatingLogger()
        } else {
            floatingLoggerManager.disableFloatingLogger()
        }
        settingsRepository[SharedPrefsItem.ShowLogger] = enabled
    }

    override fun onLanguageStrategyChanged(strategy: String) {
        val isCustom = strategy == "Custom"
        _enabledState.value = _enabledState.value.copy(
            customLanguageEnabled = isCustom
        )
    }

    override fun onArticleInformationStrategyChanged(strategy: String) {
        val isLocal = strategy == "Local"
        _enabledState.value = _enabledState.value.copy(
            articleAssociatedUrlEnabled = isLocal
        )
    }

    override fun onPreConversationStyleChanged(style: String) {
        val isCustom = style == "Custom"
        _enabledState.value = _enabledState.value.copy(
            preConversationCustomOptionsEnabled = isCustom
        )
    }

    override fun onConversationStyleChanged(style: String) {
        val isCustom = style == "Custom"
        _enabledState.value = _enabledState.value.copy(
            conversationCustomOptionsEnabled = isCustom,
            // Disable spacing fields when conversation style is not custom
            conversationSpacingCustomFieldsEnabled =
            if (!isCustom) false else _enabledState.value.conversationSpacingCustomFieldsEnabled
        )
    }

    override fun onConversationSpacingStyleChanged(style: String) {
        val isCustomSpacing = style == "Custom"
        val isConvCustom = _enabledState.value.conversationCustomOptionsEnabled
        _enabledState.value = _enabledState.value.copy(
            conversationSpacingCustomFieldsEnabled = isConvCustom && isCustomSpacing
        )
    }
}
