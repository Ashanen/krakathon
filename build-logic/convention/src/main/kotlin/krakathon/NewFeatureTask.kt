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
        if (!Regex("[a-z][a-z0-9]*").matches(name)) throw GradleException("Feature name must match [a-z][a-z0-9]*, got '$name'.")
        val root = rootDirectory.get().asFile
        val featureDir = File(root, "feature/$name")
        if (featureDir.exists()) throw GradleException("feature/$name already exists.")

        FeatureTemplates(name).files().forEach { (path, content) ->
            File(featureDir, path).apply { parentFile.mkdirs() }.writeText(content)
        }
        addToSettings(File(root, "settings.gradle.kts"), name)
        insertBefore(File(root, "app/build.gradle.kts"), "// newFeature:dependencies", LAYERS.map { "implementation(projects.feature.$name.$it)" })
        insertBefore(
            File(root, "app/src/main/kotlin/com/castorama/krakathon/AppModules.kt"),
            "// newFeature:modules",
            listOf("${name}DomainModule,", "${name}DataModule,", "${name}UiModule,"),
        )
        addAppImports(File(root, "app/src/main/kotlin/com/castorama/krakathon/AppModules.kt"), name)
        logger.lifecycle("Created feature/$name. Open ${name.capitalized()}Route in the app via navigator.navigate(${name.capitalized()}Route).")
    }

    private fun addToSettings(settings: File, name: String) {
        settings.appendText("\n" + LAYERS.joinToString("\n") { "include(\":feature:$name:$it\")" } + "\n")
    }

    private fun insertBefore(file: File, marker: String, lines: List<String>) {
        val text = file.readText()
        val markerLine = text.lines().firstOrNull { it.trim() == marker } ?: throw GradleException("Marker '$marker' missing in ${file.path}.")
        val indent = markerLine.takeWhile { it == ' ' }
        file.writeText(text.replace(markerLine, lines.joinToString("") { "$indent$it\n" } + markerLine))
    }

    private fun addAppImports(file: File, name: String) {
        val imports = listOf("domain", "data", "ui").joinToString("") {
            "import $BASE_PACKAGE.feature.$name.$it.di.$name${it.capitalized()}Module\n"
        }
        val text = file.readText()
        val firstImport = text.indexOf("import ")
        file.writeText(text.substring(0, firstImport) + imports + text.substring(firstImport))
    }

    private companion object {
        val LAYERS = listOf("api", "domain", "data", "ui")
    }
}

internal fun String.capitalized(): String = replaceFirstChar { it.uppercase() }
