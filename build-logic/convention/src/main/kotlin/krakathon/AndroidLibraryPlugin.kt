package krakathon

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/** Base for every Android library module: SDK levels, JVM 17, Kotest on JUnit 5. */
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(plugin("android-library"))

        extensions.configure<LibraryExtension> {
            namespace = namespaceFromPath()
            compileSdk = version("compileSdk").toInt()
            defaultConfig.minSdk = version("minSdk").toInt()
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            testOptions.unitTests.isReturnDefaultValues = true
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            testLogging {
                events("failed")
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }

        impl(lib("kotlinx-coroutines-core"))
        testImpl(lib("kotest-runner"))
        testImpl(lib("kotest-assertions"))
        testImpl(lib("kotlinx-coroutines-test"))
        testImpl(lib("turbine"))
        if (path != ":core:testing") testImpl(project(":core:testing"))
    }
}
