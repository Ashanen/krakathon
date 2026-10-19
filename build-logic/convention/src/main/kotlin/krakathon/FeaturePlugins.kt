package krakathon

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * One plugin per feature layer. Each wires its own-feature modules and allowed core modules,
 * so a feature build file only lists cross-feature `api` deps.
 */
class FeatureApiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryPlugin::class.java)
        pluginManager.apply(plugin("kotlin-serialization"))
        api(project(":core:usecase"))
        api(project(":core:exception"))
        api(project(":core:events"))
        api(lib("androidx-navigation3-runtime"))
        api(lib("kotlinx-serialization-json"))
    }
}

class FeatureDomainPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryPlugin::class.java)
        api(project(":feature:$featureName:api"))
        impl(project(":core:usecase"))
        impl(project(":core:exception"))
        impl(project(":core:events"))
        impl(lib("koin-core"))
    }
}

class FeatureDataPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryPlugin::class.java)
        pluginManager.apply(plugin("kotlin-serialization"))
        impl(project(":feature:$featureName:domain"))
        impl(project(":core:exception"))
        impl(project(":core:events"))
        impl(project(":core:network"))
        impl(lib("koin-core"))
        impl(lib("ktor-client-core"))
        impl(lib("kotlinx-serialization-json"))
        testImpl(lib("ktor-client-mock"))
    }
}

class FeatureUiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryPlugin::class.java)
        pluginManager.apply(ComposePlugin::class.java)
        impl(project(":feature:$featureName:domain"))
        impl(project(":core:usecase"))
        impl(project(":core:exception"))
        impl(project(":core:presentation"))
        impl(project(":core:designsystem"))
        impl(project(":core:navigation"))
        impl(lib("koin-core"))
        impl(lib("koin-compose-viewmodel"))
        impl(lib("androidx-lifecycle-viewmodel-compose"))
        impl(lib("androidx-lifecycle-runtime-compose"))
        impl(lib("androidx-navigation3-runtime"))
    }
}
