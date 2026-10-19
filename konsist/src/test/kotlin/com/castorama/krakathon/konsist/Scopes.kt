package com.castorama.krakathon.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration

const val BASE = "com.castorama.krakathon"

val production: List<KoFileDeclaration> by lazy {
    Konsist.scopeFromProduction().files.filterNot { "/build-logic/" in it.path }
}

val KoFileDeclaration.pkg: String get() = packagee?.name.orEmpty()

/** `feature.<name>.<layer>` from the package, or null outside features. */
val KoFileDeclaration.feature: String? get() = pkg.removePrefix("$BASE.feature.").takeIf { it != pkg }?.substringBefore('.')
val KoFileDeclaration.layer: String? get() = feature?.let { pkg.removePrefix("$BASE.feature.$it.").substringBefore('.') }

/** Code without comments, so KDoc that mentions a banned call does not trip a rule. */
val KoFileDeclaration.code: String get() = text
    .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")
    .replace(Regex("//.*"), "")

fun List<KoFileDeclaration>.violating(check: (KoFileDeclaration) -> Boolean): List<String> =
    filter(check).map { it.path.substringAfter("/krakathon/") }
