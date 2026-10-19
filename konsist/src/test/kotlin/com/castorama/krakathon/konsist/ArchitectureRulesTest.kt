package com.castorama.krakathon.konsist

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty

/** Every rule prints the offending files. Fix the code, never the rule. */
class ArchitectureRulesTest : FunSpec({

    val viewModels = production.filter { it.layer == "ui" && it.name.endsWith("ViewModel") }
    val featureFiles = production.filter { it.feature != null }

    test("ViewModel constructor takes only use cases and plain ids") {
        val allowed = setOf("String", "Int", "Long", "Boolean")
        viewModels.flatMap { it.classes() }
            .filter { it.name.endsWith("ViewModel") }
            .flatMap { vm -> vm.primaryConstructor?.parameters.orEmpty().map { vm.name to it.type.name } }
            .filterNot { (_, type) -> type.endsWith("UseCase") || type in allowed }
            .shouldBeEmpty()
    }

    test("ViewModel uses launchWith - no viewModelScope.launch, first(), try, runCatching") {
        val banned = listOf("viewModelScope.launch", ".first()", "try {", "runCatching")
        viewModels.violating { file -> banned.any { it in file.code } }.shouldBeEmpty()
    }

    test("ui never touches repositories or data") {
        featureFiles.filter { it.layer == "ui" }
            .violating { file -> file.imports.any { it.name.endsWith("Repository") || ".data." in it.name } }
            .shouldBeEmpty()
    }

    test("use cases extend BaseUseCase or NoParamUseCase") {
        val bases = setOf("BaseUseCase", "NoParamUseCase")
        featureFiles.filter { it.layer == "domain" }
            .flatMap { it.classes() }
            .filter { it.name.endsWith("UseCase") || it.name.endsWith("UseCaseImpl") }
            .filterNot { cls -> cls.parents().any { it.name.substringBefore('<') in bases } }
            .map { it.name }
            .shouldBeEmpty()
    }

    test("domain has no Dispatchers - BaseUseCase owns threading") {
        featureFiles.filter { it.layer == "domain" }.violating { "Dispatchers." in it.code }.shouldBeEmpty()
    }

    test("domain is pure Kotlin - no android imports") {
        featureFiles.filter { it.layer == "domain" }
            .violating { file -> file.imports.any { it.name.startsWith("android") } }
            .shouldBeEmpty()
    }

    test("api has no android imports except navigation keys") {
        featureFiles.filter { it.layer == "api" }
            .violating { file ->
                file.imports.any { it.name.startsWith("android") && !it.name.startsWith("androidx.navigation3.runtime") }
            }
            .shouldBeEmpty()
    }

    test("DataSource wraps calls in safeApiCall - no try or runCatching") {
        production.filter { it.name.endsWith("DataSource") }
            .violating { "try {" in it.code || "runCatching" in it.code }
            .shouldBeEmpty()
    }

    test("another feature is imported only through its api") {
        featureFiles.violating { file ->
            file.imports.any { import ->
                val other = import.name.removePrefix("$BASE.feature.")
                other != import.name &&
                    other.substringBefore('.') != file.feature &&
                    other.split('.').getOrNull(1) != "api"
            }
        }.shouldBeEmpty()
    }

    test("no hardcoded user-facing text - use stringResource") {
        featureFiles.filter { it.layer == "ui" }
            .violating { Regex("""Text\(\s*(text\s*=\s*)?"""").containsMatchIn(it.code) }
            .shouldBeEmpty()
    }

    test("no TODO or FIXME in production code - open an issue") {
        production.violating { Regex("//\\s*(TODO|FIXME)").containsMatchIn(it.text) }.shouldBeEmpty()
    }

    test("no 'common' package - name modules by concern") {
        production.violating { file -> file.pkg.split('.').any { it.equals("common", ignoreCase = true) } }
            .shouldBeEmpty()
    }
})
