package krakathon

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

/**
 * Root plugin:
 * - checks [ModuleRules] for every module while Gradle configures (any build fails on a bad edge),
 * - `./gradlew checkModules` runs just that check,
 * - `./gradlew newFeature --name=wishlist` scaffolds a 4-module feature.
 */
class RootPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        subprojects {
            afterEvaluate { checkModuleRules() }
        }
        tasks.register("checkModules") {
            group = "verification"
            description = "Fails when a module breaks the dependency rules (checked during configuration)."
            doLast { logger.lifecycle("Module rules OK.") }
        }
        tasks.register("newFeature", NewFeatureTask::class.java) {
            group = "krakathon"
            description = "Creates feature/<name>/{api,domain,data,ui}. Usage: ./gradlew newFeature --name=wishlist"
            rootDirectory.set(layout.projectDirectory)
        }
        Unit
    }

    private fun Project.checkModuleRules() {
        val errors = configurations
            .flatMap { configuration -> configuration.dependencies.withType(ProjectDependency::class.java) }
            .map { it.path }
            .filter { it != path }
            .toSet()
            .mapNotNull { to -> ModuleRules.violation(path, to)?.let { "  $path -> $to : $it" } }
        if (errors.isNotEmpty()) {
            throw GradleException("Forbidden module dependency:\n" + errors.joinToString("\n"))
        }
    }
}
