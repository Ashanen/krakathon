package krakathon

/**
 * Pure planning for `newFeature`: validates everything and computes every file's final content
 * BEFORE anything is written. Any problem -> [IllegalArgumentException], repo untouched.
 */
internal object FeaturePlan {

    const val SETTINGS = "settings.gradle.kts"
    const val APP_BUILD = "app/build.gradle.kts"
    const val APP_MODULES = "app/src/main/kotlin/com/castorama/krakathon/AppModules.kt"
    const val DEPENDENCIES_MARKER = "// newFeature:dependencies"
    const val MODULES_MARKER = "// newFeature:modules"

    private val LAYERS = listOf("api", "domain", "data", "ui")
    private val NAME = Regex("[a-z][a-z0-9]*")

    /** Hard keywords break packages and accessors; the rest are names we keep out of the codebase. */
    private val RESERVED = setOf(
        "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in", "interface",
        "is", "null", "object", "package", "return", "super", "this", "throw", "true", "try", "typealias",
        "typeof", "val", "var", "when", "while",
        "common", "shared", "util", "utils", "misc", "base", "core", "app", "feature",
    )

    /** @return error message, or null when [name] is usable. */
    fun nameError(name: String): String? = when {
        !NAME.matches(name) -> "Feature name must match [a-z][a-z0-9]*, got '$name'."
        name in RESERVED -> "'$name' is reserved (Kotlin keyword or banned name). Pick a domain word, e.g. wishlist."
        else -> null
    }

    /**
     * @param existing current text of [SETTINGS], [APP_BUILD], [APP_MODULES] (null = file missing)
     * @return repo-relative path -> full new content, for new feature files AND edited app files
     */
    fun plan(name: String, featureExists: Boolean, existing: (String) -> String?): Map<String, String> {
        nameError(name)?.let { throw IllegalArgumentException(it) }
        require(!featureExists) { "feature/$name already exists." }
        val settings = requireFile(existing, SETTINGS)
        val appBuild = requireFile(existing, APP_BUILD)
        val appModules = requireFile(existing, APP_MODULES)
        val newFiles = FeatureTemplates(name).files().mapKeys { (path, _) -> "feature/$name/$path" }
        return newFiles + mapOf(
            SETTINGS to settings.trimEnd() + "\n\n" + LAYERS.joinToString("\n") { "include(\":feature:$name:$it\")" } + "\n",
            APP_BUILD to insertBefore(appBuild, APP_BUILD, DEPENDENCIES_MARKER, LAYERS.map { "implementation(projects.feature.$name.$it)" }),
            APP_MODULES to addImports(
                insertBefore(appModules, APP_MODULES, MODULES_MARKER, listOf("${name}DomainModule,", "${name}DataModule,", "${name}UiModule,")),
                name,
            ),
        )
    }

    private fun requireFile(existing: (String) -> String?, path: String): String =
        requireNotNull(existing(path)) { "$path not found. Run newFeature from the repo root." }

    private fun insertBefore(text: String, path: String, marker: String, lines: List<String>): String {
        val markerLine = text.lines().firstOrNull { it.trim() == marker }
            ?: throw IllegalArgumentException("Marker '$marker' missing in $path. Put it back where new entries go.")
        val indent = markerLine.takeWhile { it == ' ' }
        return text.replaceFirst(markerLine, lines.joinToString("") { "$indent$it\n" } + markerLine)
    }

    private fun addImports(text: String, name: String): String {
        val firstImport = text.indexOf("\nimport ").takeIf { it >= 0 }?.plus(1)
            ?: throw IllegalArgumentException("$APP_MODULES has no imports to extend.")
        val imports = listOf("domain", "data", "ui").joinToString("") {
            "import $BASE_PACKAGE.feature.$name.$it.di.$name${it.capitalized()}Module\n"
        }
        return text.substring(0, firstImport) + imports + text.substring(firstImport)
    }
}
