package krakathon

/**
 * The module graph law. `app` and `konsist` may use anything.
 * Core modules may use only core. Features talk to each other only through `api`.
 */
internal object ModuleRules {

    private val coreAllowedPerLayer = mapOf(
        "api" to setOf("usecase", "exception", "events", "testing"),
        "domain" to setOf("usecase", "exception", "events", "testing"),
        "data" to setOf("exception", "events", "network", "testing"),
        "ui" to setOf("usecase", "exception", "presentation", "designsystem", "navigation", "testing"),
    )

    private val ownLayerAllowed = mapOf(
        "api" to emptySet(),
        "domain" to setOf("api"),
        "data" to setOf("api", "domain"),
        "ui" to setOf("api", "domain"),
    )

    /** Returns null when allowed, otherwise a one-line reason. */
    fun violation(from: String, to: String): String? {
        val source = Module.parse(from)
        val target = Module.parse(to)
        return when (source) {
            Module.Free -> null
            is Module.Core -> when (target) {
                is Module.Core -> null
                else -> "core modules must not depend on features or app."
            }
            is Module.Feature -> featureViolation(source, target)
        }
    }

    private fun featureViolation(source: Module.Feature, target: Module): String? = when (target) {
        Module.Free -> "features must not depend on app."
        is Module.Core -> if (target.name in coreAllowedPerLayer.getValue(source.layer)) null
        else "${source.layer} may use only ${coreAllowedPerLayer.getValue(source.layer).joinToString { ":core:$it" }}."
        is Module.Feature -> when {
            source.layer == "api" -> "api must not depend on any feature. Keep api small: models, routes, interfaces."
            target.name == source.name -> if (target.layer in ownLayerAllowed.getValue(source.layer)) null
            else "${source.layer} must not use its own ${target.layer}."
            target.layer == "api" -> null
            else -> "other features only through :feature:${target.name}:api."
        }
    }

    private sealed interface Module {
        data object Free : Module
        data class Core(val name: String) : Module
        data class Feature(val name: String, val layer: String) : Module

        companion object {
            fun parse(path: String): Module {
                val parts = path.removePrefix(":").split(':')
                return when {
                    parts.size == 3 && parts[0] == "feature" -> Feature(parts[1], parts[2])
                    parts.size >= 2 && parts[0] == "core" -> Core(parts[1])
                    else -> Free
                }
            }
        }
    }
}
