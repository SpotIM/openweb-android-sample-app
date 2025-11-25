package openweb.sample.ui.screens.settings

import android.content.Context
import spotIm.common.internal.model.settings.OWEnvironment

/**
 * No-op stub for environment changes in public builds.
 * Environment configuration is internal-only functionality.
 */
fun changeEnvironment(context: Context, environment: OWEnvironment, environmentBaseUrl: String?) = Unit

/**
 * No-op stub for config clearing in public builds.
 * Advanced configuration reset is internal-only.
 */
fun clearConfig() = Unit

/**
 * No-op stub for login delegation clearing in public builds.
 * Login delegation management is internal-only.
 */
fun clearLoginDelegation() = Unit
