package krakathon

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain

class FeaturePlanTest : BehaviorSpec({

    val repo = mapOf(
        FeaturePlan.SETTINGS to "rootProject.name = \"krakathon\"\n",
        FeaturePlan.APP_BUILD to "dependencies {\n    implementation(x)\n    // newFeature:dependencies\n}\n",
        FeaturePlan.APP_MODULES to "package app\n\nimport a.b\n\nval appModules = listOf(\n    a,\n    // newFeature:modules\n)\n",
    )

    Given("feature names") {
        Then("plain lowercase words are accepted") {
            listOf("wishlist", "stores2", "x").forEach { FeaturePlan.nameError(it).shouldBeNull() }
        }
        Then("bad shape is rejected") {
            listOf("Wishlist", "2fa", "wish-list", "wish_list", "").forEach { FeaturePlan.nameError(it).shouldNotBeNull() }
        }
        Then("Kotlin keywords are rejected") {
            listOf("class", "when", "object", "fun", "in", "is").forEach { FeaturePlan.nameError(it).shouldNotBeNull() }
        }
        Then("banned generic names are rejected") {
            listOf("common", "shared", "utils", "core").forEach { FeaturePlan.nameError(it).shouldNotBeNull() }
        }
    }

    Given("a healthy repo") {
        val plan = FeaturePlan.plan("wishlist", featureExists = false) { repo[it] }

        Then("all 4 modules and the 3 app files are planned") {
            plan.keys shouldContainAll listOf(
                "feature/wishlist/api/build.gradle.kts",
                "feature/wishlist/domain/build.gradle.kts",
                "feature/wishlist/data/build.gradle.kts",
                "feature/wishlist/ui/build.gradle.kts",
                FeaturePlan.SETTINGS,
                FeaturePlan.APP_BUILD,
                FeaturePlan.APP_MODULES,
            )
        }
        Then("app files are wired before the markers") {
            plan.getValue(FeaturePlan.SETTINGS) shouldContain "include(\":feature:wishlist:ui\")"
            plan.getValue(FeaturePlan.APP_BUILD) shouldContain "    implementation(projects.feature.wishlist.ui)\n    // newFeature:dependencies"
            plan.getValue(FeaturePlan.APP_MODULES) shouldContain "    wishlistUiModule,\n    // newFeature:modules"
            plan.getValue(FeaturePlan.APP_MODULES) shouldContain "import com.castorama.krakathon.feature.wishlist.ui.di.wishlistUiModule\nimport a.b"
        }
    }

    Given("a broken repo") {
        Then("an existing feature fails before planning anything") {
            shouldThrow<IllegalArgumentException> { FeaturePlan.plan("wishlist", featureExists = true) { repo[it] } }
                .message shouldContain "already exists"
        }
        Then("a missing marker fails before planning anything") {
            val noMarker = repo + (FeaturePlan.APP_MODULES to "package app\n\nimport a.b\n\nval appModules = listOf(a)\n")
            shouldThrow<IllegalArgumentException> { FeaturePlan.plan("wishlist", featureExists = false) { noMarker[it] } }
                .message shouldContain FeaturePlan.MODULES_MARKER
        }
        Then("a missing app file fails before planning anything") {
            shouldThrow<IllegalArgumentException> { FeaturePlan.plan("wishlist", featureExists = false) { null } }
                .message shouldContain "not found"
        }
        Then("a keyword name fails before planning anything") {
            shouldThrow<IllegalArgumentException> { FeaturePlan.plan("class", featureExists = false) { repo[it] } }
        }
    }
})
