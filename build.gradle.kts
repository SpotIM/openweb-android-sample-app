buildscript {
    repositories {
        mavenCentral()
        google()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlinx.serialization.gradle.plugin)
        classpath(libs.android.maven.gradle.plugin)
        classpath(libs.javapoet)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.detekt.gradle.plugin)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinx.serialization) apply false
    id("openweb.nexus-publishing-plugin")
}

// Project version properties
extra["sample_version_name"] = "2.5.0.4-rn"
extra["sdk_version_name"] = "2.5.0"
extra["rn_sdk_version_name"] = "1.22.7"
extra["build_number"] = 118

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    apply(from = rootProject.file("detekt.gradle"))
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        ignoreFailures.set(false)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
        }
        disabledRules.set(setOf("max-line-length", "trailing-comma-on-declaration-site", "final-newline"))
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}
