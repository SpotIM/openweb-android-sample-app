package openweb.sample.ui.screens.settings

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import openweb.sample.utils.PreferenceKey

/**
 * Extension function for type-safe preference lookup using PreferenceKey.
 * Usage:
 * ```
 * pref<ChipGroupPreference>(PreferenceKey.LanguageStrategy)?.setOnPreferenceChangeListener { ... }
 * ```
 */
inline fun <reified T : Preference> PreferenceFragmentCompat.pref(key: PreferenceKey): T? =
    findPreference(key.key)
