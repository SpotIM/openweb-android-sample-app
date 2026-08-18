package com.openweb.gradle

data class SoftwareComponentParts(
    val name: String,
    val capitalName: String,
    val isRelease: Boolean,
    val flavorSDK: SDKFlavor
)
