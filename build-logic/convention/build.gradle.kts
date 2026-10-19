import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.castorama.krakathon.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compilerGradlePlugin)
    compileOnly(libs.serialization.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("root") {
            id = "krakathon.root"
            implementationClass = "krakathon.RootPlugin"
        }
        register("androidLibrary") {
            id = "krakathon.android.library"
            implementationClass = "krakathon.AndroidLibraryPlugin"
        }
        register("compose") {
            id = "krakathon.compose"
            implementationClass = "krakathon.ComposePlugin"
        }
        register("featureApi") {
            id = "krakathon.feature.api"
            implementationClass = "krakathon.FeatureApiPlugin"
        }
        register("featureDomain") {
            id = "krakathon.feature.domain"
            implementationClass = "krakathon.FeatureDomainPlugin"
        }
        register("featureData") {
            id = "krakathon.feature.data"
            implementationClass = "krakathon.FeatureDataPlugin"
        }
        register("featureUi") {
            id = "krakathon.feature.ui"
            implementationClass = "krakathon.FeatureUiPlugin"
        }
    }
}
