package krakathon

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

const val BASE_PACKAGE = "com.castorama.krakathon"

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.lib(alias: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(alias).orElseThrow { IllegalArgumentException("No library '$alias' in libs.versions.toml") }

internal fun Project.version(alias: String): String =
    libs.findVersion(alias).orElseThrow { IllegalArgumentException("No version '$alias' in libs.versions.toml") }.requiredVersion

internal fun Project.plugin(alias: String): String =
    libs.findPlugin(alias).orElseThrow { IllegalArgumentException("No plugin '$alias' in libs.versions.toml") }.get().pluginId

/** `:feature:cart:ui` -> `com.castorama.krakathon.feature.cart.ui` */
internal fun Project.namespaceFromPath(): String =
    BASE_PACKAGE + path.replace(':', '.').replace('-', '_')

internal fun Project.impl(dependency: Any) {
    dependencies.add("implementation", dependency)
}

internal fun Project.api(dependency: Any) {
    dependencies.add("api", dependency)
}

internal fun Project.testImpl(dependency: Any) {
    dependencies.add("testImplementation", dependency)
}

/** `:feature:cart:ui` -> `cart` */
internal val Project.featureName: String
    get() = path.split(':').getOrElse(2) { error("$path is not a feature module") }
