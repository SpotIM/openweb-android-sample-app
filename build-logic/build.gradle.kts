plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.7.1")
    implementation("io.github.gradle-nexus:publish-plugin:1.3.0")
}

