package openweb.sample.ui.screens.settings

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.Parcelize
import openweb.sample.R
import openweb.sample.ui.mainactivity.MainActivity
import openweb.sample.ui.navigation.FragmentNavigator
import openweb.sample.ui.screens.settings.customtheme.CustomDarkColorState
import openweb.sample.ui.screens.settings.preference.ButtonPreference
import openweb.sample.ui.screens.settings.preference.ChipGroupPreference
import openweb.sample.ui.screens.settings.preference.ClearableEditTextPreference
import openweb.sample.ui.screens.settings.preference.ClearableEditTextPreferenceDialogFragment
import openweb.sample.ui.screens.settings.preference.ColorPreviewPreference
import openweb.sample.ui.screens.settings.preference.InlineEditTextPreference
import openweb.sample.utils.PreferenceKey
import openweb.sample.utils.collectLatestLifecycleFlow
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import spotIm.common.internal.helpers.getParcelableObject

/**
 * Settings screen using AndroidX Preference library for hierarchical configuration.
 *
 * Displays different settings categories based on [Arguments.screen]:
 * - Main settings with navigation to subsections
 * - SDK customizations (themes, colors, fonts)
 * - Authentication configuration
 * - Article and screen settings
 * - Internal debugging tools (internal builds only)
 *
 * Settings are persisted via [SettingsRepository] and applied to the SDK in real-time.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val preferenceDataStore: PreferenceDataStore by inject()
    private val viewModel: SettingsVMContract by viewModel<SettingsVM>()
    private val navigator: FragmentNavigator by inject { parametersOf(requireActivity()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val args = getArgs()

        requireActivity().title = getScreenTitle(args.screen)

        preferenceManager.preferenceDataStore = preferenceDataStore

        val xmlResId = when (args.screen) {
            SettingsScreen.MainSettings -> R.xml.preferences_root
            SettingsScreen.Customizations -> R.xml.preferences_customizations
            SettingsScreen.Authentication -> R.xml.preferences_authentication
            SettingsScreen.Configurations -> R.xml.preferences_configurations
            SettingsScreen.ArticleSettings -> R.xml.preferences_article
            SettingsScreen.ScreensSettings -> R.xml.preferences_screens
            SettingsScreen.InternalSettings -> R.xml.preferences_internal
        }

        setPreferencesFromResource(xmlResId, rootKey)

        when (args.screen) {
            SettingsScreen.MainSettings -> setupRootPreferences()
            SettingsScreen.Customizations -> setupCustomizationsPreferences()
            SettingsScreen.Authentication -> Unit
            SettingsScreen.Configurations -> setupConfigurationsPreferences()
            SettingsScreen.ArticleSettings -> setupArticlePreferences()
            SettingsScreen.ScreensSettings -> setupScreensPreferences()
            SettingsScreen.InternalSettings -> setupInternalPreferences()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = getScreenTitle(getArgs().screen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()

        setupToolbarMenu()
        observeFlows()
        setupContentDescriptions()
    }

    private fun setupContentDescriptions() {
        listView.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    view.findViewById<TextView>(android.R.id.title)
                        ?.text
                        ?.let { view.contentDescription = it.toString().replace(" ", "") }
                }

                @Suppress("EmptyFunctionBlock")
                override fun onChildViewDetachedFromWindow(view: View) { }
            }
        )
    }

    private fun setupToolbarMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_settings, menu)
                }

                override fun onPrepareMenu(menu: Menu) {
                    menu.findItem(R.id.settings)?.isVisible = false
                    menu.findItem(R.id.auth)?.isVisible = false
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == R.id.reset_settings) {
                        viewModel.inputs.onResetSettingsClicked()
                        true
                    } else {
                        false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is ClearableEditTextPreference) {
            val dialogFragment = ClearableEditTextPreferenceDialogFragment.newInstance(preference.key)
            @Suppress("DEPRECATION")
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, "ClearableEditTextPreferenceDialog")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private fun getArgs(): Arguments =
        arguments?.getParcelableObject(EXTRA_ARGS, Arguments::class.java)
            ?: Arguments(SettingsScreen.MainSettings)

    private fun getScreenTitle(screen: SettingsScreen): String =
        when (screen) {
            SettingsScreen.MainSettings -> "Settings"
            SettingsScreen.Customizations -> "Customizations"
            SettingsScreen.Authentication -> "Authentication"
            SettingsScreen.Configurations -> "Configurations"
            SettingsScreen.ArticleSettings -> "Article Settings"
            SettingsScreen.ScreensSettings -> "Screens Settings"
            SettingsScreen.InternalSettings -> "Internal Settings"
        }

    private fun setupRootPreferences() {
        setupNavigationPreference(PreferenceKey.Customizations, SettingsScreen.Customizations)
        setupNavigationPreference(PreferenceKey.Authentication, SettingsScreen.Authentication)
        setupNavigationPreference(PreferenceKey.Configurations, SettingsScreen.Configurations)
        setupNavigationPreference(PreferenceKey.ArticleSettings, SettingsScreen.ArticleSettings)
        setupNavigationPreference(PreferenceKey.ScreensSettings, SettingsScreen.ScreensSettings)
        setupNavigationPreference(PreferenceKey.InternalSettings, SettingsScreen.InternalSettings)
    }

    private fun setupNavigationPreference(key: PreferenceKey, screen: SettingsScreen) {
        pref<Preference>(key)?.setOnPreferenceClickListener {
            navigator.navigateToNestedSettingsMenu(screen)
            true
        }
    }

    private fun setupCustomizationsPreferences() {
        pref<ColorPreviewPreference>(PreferenceKey.CustomDarkColor)?.setOnPreferenceClickListener {
            viewModel.inputs.onCustomDarkColorClicked()
            true
        }

        pref<Preference>(PreferenceKey.CustomThemeColors)?.setOnPreferenceClickListener {
            navigator.navigateToCustomThemePicker()
            true
        }
    }

    private fun setupConfigurationsPreferences() {
        pref<ChipGroupPreference>(PreferenceKey.LanguageStrategy)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onLanguageStrategyChanged(newValue as String)
            true
        }
    }

    private fun setupArticlePreferences() {
        pref<ChipGroupPreference>(PreferenceKey.ArticleStrategy)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onArticleInformationStrategyChanged(newValue as String)
            true
        }
    }

    private fun setupScreensPreferences() {
        pref<ChipGroupPreference>(PreferenceKey.PreConvStyle)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onPreConversationStyleChanged(newValue as String)
            true
        }

        pref<ChipGroupPreference>(PreferenceKey.ConvStyle)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onConversationStyleChanged(newValue as String)
            true
        }

        pref<ChipGroupPreference>(PreferenceKey.ConvSpacingStyle)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onConversationSpacingStyleChanged(newValue as String)
            true
        }

        val spacingValidator: (String) -> String? = { value -> viewModel.inputs.validateSpacing(value) }

        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingBetweenComments)?.validator = spacingValidator
        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingGuidelines)?.validator = spacingValidator
        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingQuestions)?.validator = spacingValidator
    }

    private fun setupInternalPreferences() {
        setupEnvironmentPreferences()

        pref<SwitchPreferenceCompat>(PreferenceKey.LoggerView)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.inputs.onLoggerViewChanged(newValue as Boolean)
            true
        }

        pref<Preference>(PreferenceKey.EndpointOverrides)?.setOnPreferenceClickListener {
            navigator.navigateToEndpointOverride()
            true
        }

        pref<Preference>(PreferenceKey.ClearConfig)?.setOnPreferenceClickListener {
            viewModel.inputs.clearConfigButtonClicked()
            Toast.makeText(requireContext(), R.string.config_cleared, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupEnvironmentPreferences() {
        val environmentPref = pref<ChipGroupPreference>(PreferenceKey.Environment) ?: return
        val baseUrlPref = pref<InlineEditTextPreference>(PreferenceKey.EnvironmentBaseUrl)
        val applyButtonPref = pref<ButtonPreference>(PreferenceKey.ApplyBaseUrl)

        baseUrlPref?.validator = { url -> viewModel.inputs.validateUrl(url).second }

        environmentPref.setOnPreferenceChangeListener { _, newValue ->
            environmentPref.setValue(newValue as String)
            viewModel.inputs.onEnvironmentChanged(newValue)
            false // Don't persist - only Apply button persists
        }

        applyButtonPref?.setOnPreferenceClickListener {
            val selectedEnv = environmentPref.getValue() ?: return@setOnPreferenceClickListener false
            val baseUrl = baseUrlPref?.getText()
            viewModel.inputs.onApplyEnvironmentClicked(selectedEnv, baseUrl)
            true
        }
    }

    private fun triggerRestart() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
        Runtime.getRuntime().exit(0)
    }

    private fun reloadPreferences() {
        val args = getArgs()

        val xmlResId = when (args.screen) {
            SettingsScreen.MainSettings -> R.xml.preferences_root
            SettingsScreen.Customizations -> R.xml.preferences_customizations
            SettingsScreen.Authentication -> R.xml.preferences_authentication
            SettingsScreen.Configurations -> R.xml.preferences_configurations
            SettingsScreen.ArticleSettings -> R.xml.preferences_article
            SettingsScreen.ScreensSettings -> R.xml.preferences_screens
            SettingsScreen.InternalSettings -> R.xml.preferences_internal
        }

        preferenceScreen.removeAll()
        setPreferencesFromResource(xmlResId, null)

        // Re-setup listeners for the current screen
        when (args.screen) {
            SettingsScreen.MainSettings -> setupRootPreferences()
            SettingsScreen.Customizations -> setupCustomizationsPreferences()
            SettingsScreen.Authentication -> Unit
            SettingsScreen.Configurations -> setupConfigurationsPreferences()
            SettingsScreen.ArticleSettings -> setupArticlePreferences()
            SettingsScreen.ScreensSettings -> setupScreensPreferences()
            SettingsScreen.InternalSettings -> setupInternalPreferences()
        }

        // Re-apply enabled states and custom color preview to the new preference objects
        updatePreferenceEnabledState(viewModel.outputs.enabledState.value)
        updateCustomDarkColorPreview(viewModel.outputs.customDarkColorState.value)
    }

    private fun observeFlows() {
        collectLatestLifecycleFlow(viewModel.outputs.events) { event ->
            when (event) {
                is SettingsEvent.NavigateToCustomThemePicker -> navigator.navigateToCustomThemePicker()
                is SettingsEvent.NavigateToEndpointOverride -> navigator.navigateToEndpointOverride()
                is SettingsEvent.NavigateToNestedMenu -> navigator.navigateToNestedSettingsMenu(event.screen)
                is SettingsEvent.ChangeEnvironment ->
                    changeEnvironment(requireContext(), event.environment, event.baseUrl)

                is SettingsEvent.ShowToast ->
                    SettingsDialogHelper.showToast(requireContext(), event.message)

                is SettingsEvent.ShowConfirmationDialog ->
                    SettingsDialogHelper.showConfirmationDialog(
                        context = requireContext(),
                        title = event.title,
                        message = event.message,
                        onConfirm = event.onConfirm
                    )

                is SettingsEvent.ShowColorPicker ->
                    SettingsDialogHelper.showColorPickerDialog(
                        context = requireContext(),
                        onColorSelected = { viewModel.inputs.onCustomDarkColorSelected(it) },
                        onReset = { viewModel.inputs.onCustomDarkColorReset() }
                    )

                is SettingsEvent.TriggerRestart -> triggerRestart()

                is SettingsEvent.ResetSettings -> {
                    reloadPreferences()
                    Toast.makeText(requireContext(), R.string.settings_reset_success, Toast.LENGTH_SHORT).show()
                }
            }
        }

        collectLatestLifecycleFlow(viewModel.outputs.enabledState) { state ->
            updatePreferenceEnabledState(state)
        }

        collectLatestLifecycleFlow(viewModel.outputs.customDarkColorState) { state ->
            updateCustomDarkColorPreview(state)
        }
    }

    private fun updateCustomDarkColorPreview(state: CustomDarkColorState) {
        pref<ColorPreviewPreference>(PreferenceKey.CustomDarkColor)?.apply {
            summary = "Current: ${state.hexString}"
            colorValue = state.colorValue
        }
    }

    private fun updatePreferenceEnabledState(state: PreferenceEnabledState) {
        pref<Preference>(PreferenceKey.EnvironmentBaseUrl)?.isEnabled = state.environmentCustomFieldsEnabled
        pref<ChipGroupPreference>(PreferenceKey.MockSsoEnvironment)?.isEnabled = state.environmentCustomFieldsEnabled
        pref<Preference>(PreferenceKey.ApplyBaseUrl)?.isEnabled = state.applyButtonEnabled

        pref<ListPreference>(PreferenceKey.CustomLanguage)?.isEnabled = state.customLanguageEnabled

        pref<InlineEditTextPreference>(PreferenceKey.ArticleUrl)?.isEnabled = state.articleAssociatedUrlEnabled

        pref<SeekBarPreference>(PreferenceKey.PreConvNumComments)?.isEnabled = state.preConversationCustomOptionsEnabled
        pref<ChipGroupPreference>(PreferenceKey.PreConvGuidelinesStyle)?.isEnabled =
            state.preConversationCustomOptionsEnabled
        pref<ChipGroupPreference>(PreferenceKey.PreConvQuestionsStyle)?.isEnabled =
            state.preConversationCustomOptionsEnabled

        pref<ChipGroupPreference>(PreferenceKey.ConvGuidelinesStyle)?.isEnabled = state.conversationCustomOptionsEnabled
        pref<ChipGroupPreference>(PreferenceKey.ConvQuestionsStyle)?.isEnabled = state.conversationCustomOptionsEnabled
        pref<ChipGroupPreference>(PreferenceKey.ConvSpacingStyle)?.isEnabled = state.conversationCustomOptionsEnabled

        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingBetweenComments)?.isEnabled =
            state.conversationSpacingCustomFieldsEnabled
        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingGuidelines)?.isEnabled =
            state.conversationSpacingCustomFieldsEnabled
        pref<InlineEditTextPreference>(PreferenceKey.ConvSpacingQuestions)?.isEnabled =
            state.conversationSpacingCustomFieldsEnabled
    }

    @Parcelize
    data class Arguments(
        val screen: SettingsScreen = SettingsScreen.MainSettings
    ) : Parcelable

    companion object {
        private const val EXTRA_ARGS = "extra_args"

        fun newInstance(screen: SettingsScreen = SettingsScreen.MainSettings) = SettingsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ARGS, Arguments(screen))
            }
        }
    }
}
