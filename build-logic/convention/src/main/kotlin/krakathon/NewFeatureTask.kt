package krakathon

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

/** `./gradlew newFeature --name=wishlist` -> feature/wishlist/{api,domain,data,ui} wired into settings + app. */
abstract class NewFeatureTask : DefaultTask() {

    @get:Input
    @get:Option(option = "name", description = "Feature name, lowercase letters/digits, e.g. wishlist")
    abstract val featureName: Property<String>

    @get:Internal
    abstract val rootDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val name = featureName.get()
        val root = rootDirectory.get().asFile
        val plan = try {
            FeaturePlan.plan(name, File(root, "feature/$name").exists()) { path -> File(root, path).takeIf { it.isFile }?.readText() }
        } catch (e: IllegalArgumentException) {
            throw GradleException(e.message ?: "newFeature failed.", e)
        }
        plan.forEach { (path, content) -> File(root, path).apply { parentFile.mkdirs() }.writeText(content) }
        logger.lifecycle("Created feature/$name. Open it with navigator.navigate(${name.capitalized()}Route).")
    }
}

internal fun String.capitalized(): String = replaceFirstChar { it.uppercase() }
