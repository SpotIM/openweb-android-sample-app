package com.openweb.gradle.utils

import org.gradle.api.component.SoftwareComponent

val SoftwareComponent.capitalName: String get() = name.replaceFirstChar(Char::titlecase)
