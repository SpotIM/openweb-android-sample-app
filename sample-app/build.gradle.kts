plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

fun isPrivateRepo(): Boolean = project.findProject(":spotim-sdk") != null

android {
    namespace = "openweb.sample"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        compileSdk = 36
        targetSdk = 36
        minSdk = 23

        applicationId = if (isPrivateRepo()) {
            "im.spot.sample"
        } else {
            "com.openweb.showcase"
        }
        versionCode = rootProject.extra["build_number"] as Int
        versionName = rootProject.extra["sample_version_name"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        fun String.runCommand(): String =
            Runtime.getRuntime().exec(this).inputStream.bufferedReader().readText()

        val gitBranch = try {
            "git rev-parse --abbrev-ref HEAD".runCommand().trim()
        } catch (e: Exception) {
            "unknown"
        }
        buildConfigField("String", "BRANCH_NAME", "\"$gitBranch\"")
    }

    signingConfigs {
        create("release") {
            val keystoreFile = project.findProperty("sampleAppKeystoreFile") as? String
            val storePass = project.findProperty("sampleAppStorePassword") as? String
            val keyAliasValue = project.findProperty("sampleAppKeyAlias") as? String
            val keyPass = project.findProperty("sampleAppKeyPassword") as? String

            if (keystoreFile != null && storePass != null && keyAliasValue != null && keyPass != null) {
                storeFile = file(keystoreFile)
                storePassword = storePass
                keyAlias = keyAliasValue
                keyPassword = keyPass
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // code shrinking
            isShrinkResources = true // resource shrinking
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Only use release signing if credentials are available
            if (signingConfigs.getByName("release").storeFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = sampleAppLibs.versions.compose.compiler.get()
    }

    flavorDimensions += listOf("ow_sdk", "platform")
    productFlavors {
        create("public") {
            isDefault = !isPrivateRepo()
            dimension = "ow_sdk"
        }
        if (isPrivateRepo()) {
            create("internal") {
                isDefault = true
                dimension = "ow_sdk"
            }
            create("rn") {
                dimension = "platform"
            }
        }
        create("sdk") {
            isDefault = true
            dimension = "platform"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        force("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    }
}

dependencies {
    if (isPrivateRepo()) {
        implementation(project(":spotim-sdk"))
    } else {
        implementation(sampleAppLibs.openweb.sdk)
    }

    // Firebase
    implementation(platform(sampleAppLibs.firebase.bom))
    implementation(sampleAppLibs.firebase.crashlytics)

    // Compose
    implementation(platform(sampleAppLibs.androidx.compose.bom))
    implementation(sampleAppLibs.bundles.compose)
    debugImplementation(sampleAppLibs.androidx.compose.ui.tooling)

    implementation(sharedLibs.flow.layout)

    // Rive uses startup to initialize
    implementation(sampleAppLibs.rive.android)
    implementation(sharedLibs.androidx.app.startup)
    implementation(sharedLibs.androidx.browser)

    implementation(sharedLibs.androidx.appcompat)
    implementation(sharedLibs.androidx.constraintlayout)
    implementation(sharedLibs.com.google.android.material)
    implementation(sampleAppLibs.androidx.preference.ktx)

    implementation(sampleAppLibs.androidx.lifecycle.runtime.ktx)
    implementation(sampleAppLibs.androidx.lifecycle.viewmodel.ktx)

    // Media3 for video playback
    implementation(sampleAppLibs.androidx.media3.exoplayer)
    implementation(sampleAppLibs.androidx.media3.ui)

    implementation(sharedLibs.retrofit)
    implementation(sharedLibs.retrofit.converter.kotlinx.serialization)
    implementation(sharedLibs.kotlinx.serialization.json)
    implementation(sampleAppLibs.retrofit.converter.scalars)
    implementation(sharedLibs.okhttp.logging.interceptor)

    implementation(sharedLibs.glide)
    annotationProcessor(sharedLibs.glide.compiler)

    // GIPHY
    implementation(sampleAppLibs.giphy)

    // Koin
    implementation(sampleAppLibs.bundles.koin)

    testImplementation(sharedLibs.junit)
    androidTestImplementation(sharedLibs.androidx.test.ext.junit)
    androidTestImplementation(sampleAppLibs.androidx.test.espresso.core)
    androidTestImplementation(sampleAppLibs.androidx.test.espresso.contrib)
    androidTestImplementation(sampleAppLibs.androidx.test.core)
    androidTestImplementation(sampleAppLibs.androidx.test.rules)

    // ColorPickerView for General Settings
    implementation(sampleAppLibs.colorpickerview)

    // Kotlin Reflection
    implementation(sampleAppLibs.kotlin.reflect)
}
