fun isPrivateRepo(): Boolean = file("spotim-sdk").exists()

rootProject.name = if (isPrivateRepo()) {
    "OpenWeb SDK"
} else {
    "OpenWeb Sample App"
}

include(":sample-app")
if (isPrivateRepo()) {
    include(":spotim-core", ":spotim-sdk", ":spotim-common")
}

// Include convention plugins
includeBuild("build-logic")

dependencyResolutionManagement {
    versionCatalogs {
        create("sampleAppLibs") {
            from(files("gradle/sample-app.libs.versions.toml"))
        }
        create("libs") {
            from(files("gradle/root.libs.versions.toml"))
        }
        create("sharedLibs") {
            from(files("gradle/shared.libs.versions.toml"))
        }

        if (isPrivateRepo()) {
            create("commonLibs") {
                from(files("gradle/common.libs.versions.toml"))
            }
            create("coreLibs") {
                from(files("gradle/core.libs.versions.toml"))
            }
            create("sdkLibs") {
                from(files("gradle/sdk.libs.versions.toml"))
            }
        }
    }
}
