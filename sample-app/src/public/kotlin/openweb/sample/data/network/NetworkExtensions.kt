@file:JvmName("NetworkExtensionsPublic")

package openweb.sample.data.network

import spotIm.common.internal.model.settings.OWEnvironment

/**
 * Base URL for the mock SSO authentication server in production environment.
 */
const val MOCK_SSO_BASE_URL_PRODUCTION = "https://api.spot.im/sso-mock-server/"

/**
 * Base URL for the mock SSO authentication server in staging environment (not available in public builds).
 */
const val MOCK_SSO_BASE_URL_STAGING = ""

/**
 * Returns the base URL for mock SSO authentication based on environment configuration.
 *
 * In public builds, always returns the production URL regardless of parameters.
 * The internal variant supports environment switching for testing purposes.
 *
 * @param environment Target SDK environment (unused in public builds)
 * @param mockSSOEnvironment Mock SSO environment selection (unused in public builds)
 * @return Production mock SSO base URL
 */
fun getMockSSOBaseURL(
    environment: OWEnvironment,
    mockSSOEnvironment: OWEnvironment
): String = MOCK_SSO_BASE_URL_PRODUCTION
