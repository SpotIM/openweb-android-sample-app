# OpenWeb Publishing Plugins

This directory contains Gradle convention plugins for publishing OpenWeb Android SDK artifacts to
Maven repositories.

## Plugins

### 1. `openweb.publishing-plugin`

**Purpose**: Core Maven publishing configuration for OpenWeb SDK modules.

**What it does**:

- Applies `maven-publish` and `signing` plugins
- Configures OSSRH (Sonatype) repository
- Sets up Maven publications with proper POM metadata
- Handles different SDK flavors (PUBLIC and RN) with different Maven group IDs
- Signs artifacts with PGP keys (except for local publishing)
- Only publishes release variants

**Usage**:

```gradle
apply plugin: "openweb.publishing-plugin"
```

**Required Properties**:

- `ossrhUsername` - Sonatype username (in `gradle.properties`)
- `ossrhKey` - Sonatype password/token (in `gradle.properties`)
- `SIGNING_KEY` - PGP signing key (base64 encoded, in `gradle.properties`)
- `signingPassPhrase` - PGP key passphrase (in `gradle.properties`)
- `ow_sdk_flavor` - SDK flavor to publish (`public` or `rn`, in `gradle.properties`)

**Required Extra Properties** (in root `build.gradle.kts`):

- `sdk_version_name` - Version for PUBLIC flavor
- `rn_sdk_version_name` - Version for RN flavor
- `build_number` - Build number

### 2. `openweb.nexus-publishing-plugin`

**Purpose**: Sonatype Nexus staging and release management.

**What it does**:

- Applies `io.github.gradle-nexus.publish-plugin`
- Configures Nexus staging repository
- Sets up automatic transition from staging to release
- Handles retry logic for repository operations
- Manages different package groups based on SDK flavor

**Usage**:

```gradle
id("openweb.nexus-publishing-plugin")
```

**Required Properties**:

- `ossrhUsername` - Sonatype username
- `ossrhKey` - Sonatype password/token
- `ow_sdk_flavor` - SDK flavor to determine package group

## SDK Flavors

- **PUBLIC** (`public`): Published to `io.github.spotim` group
- **RN** (`rn`): Published to `io.github.spotim.rn` group for React Native SDK

## Workflow

1. Apply `openweb.publishing-plugin` to individual modules
2. Apply `openweb.nexus-publishing-plugin` to root project
3. Set required properties in `gradle.properties` or environment
4. Run publishing tasks:
    - `publishToMavenLocal` - Local publishing (no signing)
    - `publishToSonatype` - **SAFE**: Publishes to Sonatype staging repository (NOT Maven Central)
    - Manual approval required to release from staging to Maven Central

## Example Configuration

```gradle
// In module build.gradle
apply plugin: "openweb.publishing-plugin"

// In root build.gradle
plugins {
    id("openweb.nexus-publishing-plugin")
}

// In root build.gradle.kts
extra["sdk_version_name"] = "1.0.0"
extra["rn_sdk_version_name"] = "1.0.0"
extra["build_number"] = "1"

// In gradle.properties
ow_sdk_flavor=public
ossrhUsername=your-username
ossrhKey=your-token
```
