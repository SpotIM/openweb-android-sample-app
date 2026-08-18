package com.openweb.gradle

enum class SDKFlavor(val mavenGroupId: String, val propertyValue: String) {
    PUBLIC("io.github.spotim", "public"),
    RN("io.github.spotim.rn", "rn");

    companion object {
        fun fromPropertyValue(propertyValue: String): SDKFlavor {
            return values().firstOrNull { it.propertyValue == propertyValue } ?: PUBLIC
        }
    }
}
