rootProject.name = "krakathon"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":konsist")

include(":core:usecase")
include(":core:exception")
include(":core:events")
include(":core:presentation")
include(":core:designsystem")
include(":core:navigation")
include(":core:network")
include(":core:testing")

include(":feature:catalog:api")
include(":feature:catalog:domain")
include(":feature:catalog:data")
include(":feature:catalog:ui")

include(":feature:cart:api")
include(":feature:cart:domain")
include(":feature:cart:data")
include(":feature:cart:ui")
