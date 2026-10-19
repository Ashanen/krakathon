package krakathon

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/** Adds Jetpack Compose (BOM + Material 3 + tooling) to an Android module. */
class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(plugin("kotlin-compose"))
        extensions.getByType(LibraryExtension::class.java).buildFeatures.compose = true

        val bom = dependencies.platform(lib("compose-bom").get())
        impl(bom)
        impl(lib("compose-runtime"))
        impl(lib("compose-ui"))
        impl(lib("compose-foundation"))
        impl(lib("compose-material3"))
        impl(lib("compose-material-icons"))
        impl(lib("compose-ui-tooling-preview"))
        dependencies.add("debugImplementation", lib("compose-ui-tooling"))
        Unit
    }
}
