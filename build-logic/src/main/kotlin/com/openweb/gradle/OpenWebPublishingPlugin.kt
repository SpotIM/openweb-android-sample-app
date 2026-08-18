package com.openweb.gradle

import com.openweb.gradle.utils.capitalName
import nmcp.NmcpAggregationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import java.time.Duration

class OpenWebPublishingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Apply necessary plugins if not already applied
        if (!project.plugins.hasPlugin("maven-publish")) {
            project.plugins.apply("maven-publish")
        }
        if (!project.plugins.hasPlugin("signing")) {
            project.plugins.apply("signing")
        }

        // After evaluation, configure publishing
        project.afterEvaluate {
            project.configure<PublishingExtension> {
                publications {
                    setupPublications(project, this)
                }
            }
        }
    }

    private fun setupPublications(project: Project, publications: PublicationContainer) {
        val publishFlavor = getSDKFlavorToPublish(project)

        project.afterEvaluate {
            project.components.forEach { component ->
                createMaven(project, publications, component, publishFlavor)?.apply {
                    if (requiresSigning(project, version)) {
                        sign(project, this)
                    }
                }
            }
        }
    }

    private fun createMaven(
        project: Project,
        publications: PublicationContainer,
        component: SoftwareComponent,
        publishFlavor: SDKFlavor
    ): MavenPublication? {
        val sdkVersion = (project.rootProject.extra["sdk_version_name"] as String)
        val rnSdkVersion = (project.rootProject.extra["rn_sdk_version_name"] as String)

        val componentParts = parseName(component, publishFlavor)
        val componentName = componentParts.capitalName
        val groupId = publishFlavor.mavenGroupId
        val artifactId = project.name
        val baseVersion = if (publishFlavor == SDKFlavor.RN) rnSdkVersion else sdkVersion
        val version = resolveVersion(project, baseVersion)

        if (!allowPublish(componentParts)) return null

        return publications.create("maven$componentName", MavenPublication::class.java) {
            from(component)

            this.groupId = groupId
            this.version = version
            this.artifactId = artifactId
            configurePom(project, this)
        }
    }

    private fun configurePom(project: Project, publication: MavenPublication) {
        publication.pom {
            name.set(project.name)
            description.set(
                "spotim-android-sdk enables you to create a fluent conversation experience in your " +
                        "Android app that fuels quality interactions with community and content and allows users to " +
                        "create valuable and engaging content."
            )
            url.set("https://github.com/SpotIM/spotim-android-sdk")

            licenses {
                license {
                    name.set("Proprietary Licenses")
                    url.set("https://www.openweb.com/legal-and-privacy/terms-of-use")
                }
            }
            developers {
                developer {
                    id.set("openweb")
                    name.set("OpenWeb")
                    email.set("support@openweb.com")
                }
            }
            scm {
                connection.set("scm:git:git:github.com/SpotIM/spotim-android-sdk.git")
                developerConnection.set("scm:git:ssh://github.com/SpotIM/spotim-android-sdk.git")
                url.set("https://github.com/SpotIM/spotim-android-sdk")
            }
        }
    }

    /**
     * The published version: [baseVersion] unless overridden by `sdkVersionOverride`, with `-SNAPSHOT`
     * appended when `snapshotBuild` is set. Central Portal Snapshots rejects versions without the suffix.
     */
    private fun resolveVersion(project: Project, baseVersion: String): String {
        val version = project.findProperty(PROPERTY_SDK_VERSION_OVERRIDE)
            ?.toString()
            ?.takeIf { it.isNotBlank() }
            ?: baseVersion
        val snapshotRequested = project.hasProperty(PROPERTY_SNAPSHOT_BUILD)

        return if (snapshotRequested && !version.endsWith(SNAPSHOT_SUFFIX)) {
            "$version$SNAPSHOT_SUFFIX"
        } else {
            version
        }
    }

    // Maven Local needs no signatures, and Central Portal Snapshots neither validates nor requires them.
    private fun requiresSigning(project: Project, version: String): Boolean =
        !version.endsWith(SNAPSHOT_SUFFIX) &&
                project.gradle.startParameter.taskNames.none { it.contains("ToMavenLocal") }

    private fun sign(project: Project, publication: MavenPublication) {
        val signingKey = project.findProperty("SIGNING_KEY")?.toString()
        val signingKeyPwd = project.findProperty("SIGNING_PASS_PHRASE").toString()

        project.extensions.getByType<SigningExtension>().apply {
            useInMemoryPgpKeys(signingKey, signingKeyPwd)
            sign(publication)
        }
    }

    private fun getSDKFlavorToPublish(project: Project): SDKFlavor {
        val flavorPropertyValue = project.findProperty(PROPERTY_PUBLISH_FLAVOR_SDK).toString()
        return SDKFlavor.fromPropertyValue(flavorPropertyValue)
    }

    private fun parseName(
        component: SoftwareComponent,
        publishFlavor: SDKFlavor
    ): SoftwareComponentParts {
        val name = component.name
        val capitalName = component.capitalName
        val flavorName = capitalName
        val isRelease = isReleaseVariant(flavorName)

        return SoftwareComponentParts(
            name = name,
            capitalName = capitalName,
            isRelease = isRelease,
            flavorSDK = publishFlavor
        )
    }

    private fun allowPublish(componentParts: SoftwareComponentParts): Boolean {
        if (!componentParts.isRelease) return false
        // Only publish public variants; internal variants must never reach Maven Central
        if (!componentParts.capitalName.contains("Public")) return false

        // Filter by platform flavor: only publish components matching the target flavor
        val expectedPlatformFlavor = when (componentParts.flavorSDK) {
            SDKFlavor.RN -> "Rn"
            SDKFlavor.PUBLIC -> "Sdk"
        }
        return componentParts.capitalName.contains(expectedPlatformFlavor)
    }

    private fun isReleaseVariant(flavor: String): Boolean {
        val indexRelease = flavor.lastIndexOf(VARIANT_RELEASE)
        return indexRelease >= 0
    }

    companion object {
        /** Property name for the SDK flavor to publish. */
        const val PROPERTY_PUBLISH_FLAVOR_SDK = "ow_sdk_flavor"

        /** `-PsnapshotBuild` — a flag; its presence appends `-SNAPSHOT` to the version. */
        const val PROPERTY_SNAPSHOT_BUILD = "snapshotBuild"

        /** `-PsdkVersionOverride=X` — publish X instead of the version declared in the root build. */
        const val PROPERTY_SDK_VERSION_OVERRIDE = "sdkVersionOverride"

        const val SNAPSHOT_SUFFIX = "-SNAPSHOT"

        private const val VARIANT_DEBUG = "Debug"
        private const val VARIANT_RELEASE = "Release"
    }
}

// Publishes to Sonatype's Central Portal via the nmcp aggregation plugin (token-based Publisher API,
// no staging-profile lookup — replaces the legacy OSSRH staging API Sonatype has sunset).
class OpenWebCentralPublishingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.gradleup.nmcp.aggregation")) {
            project.plugins.apply("com.gradleup.nmcp.aggregation")
        }

        configureCentralPortal(project)
    }

    private fun configureCentralPortal(project: Project) {
        val centralPortalUser = project.findProperty("CENTRAL_PORTAL_USERNAME").toString()
        val centralPortalPassword = project.findProperty("CENTRAL_PORTAL_PASSWORD").toString()
        // "AUTOMATIC" auto-releases after validation; "USER_MANAGED" uploads and waits for a manual
        // release from the Central Portal UI (used for dry runs — see deploy-sdk.yml).
        val publishingTypeValue = project.findProperty("centralPortalPublishingType")?.toString() ?: "AUTOMATIC"

        project.configure<NmcpAggregationExtension> {
            centralPortal {
                username.set(centralPortalUser)
                password.set(centralPortalPassword)
                publishingType.set(publishingTypeValue)
                if (publishingTypeValue == "AUTOMATIC") {
                    publishingTimeout.set(Duration.ofMinutes(20))
                }
            }
            // Only wire subprojects in when an aggregation task was actually requested — this walks
            // every subproject, so skip the cost on ordinary builds (assemble, ktlintCheck, tests, ...).
            if (isAggregationTaskRequested(project)) {
                // Deprecated but intentional: auto-includes every subproject that applies maven-publish
                // (spotim-sdk/core/common/compose) instead of a hardcoded module list. This build doesn't
                // use Gradle's isolated-projects feature, so the tradeoff the deprecation warns about
                // doesn't apply here.
                @Suppress("DEPRECATION")
                publishAllProjectsProbablyBreakingProjectIsolation()
            }
        }
    }

    // Matches every nmcp aggregation task: publishAggregationToCentralPortal[Snapshots],
    // nmcpPublishAggregationToMavenLocal, nmcpZipAggregation, ...
    private fun isAggregationTaskRequested(project: Project): Boolean =
        project.gradle.startParameter.taskNames.any { it.contains("Aggregation", ignoreCase = true) }
}
