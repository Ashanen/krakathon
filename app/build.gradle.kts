import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.castorama.krakathon"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.castorama.krakathon"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
        buildConfigField("String", "API_BASE_URL", "\"https://api.example.com/\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

dependencies {
    implementation(projects.core.events)
    implementation(projects.core.network)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)

    implementation(projects.feature.catalog.api)
    implementation(projects.feature.catalog.domain)
    implementation(projects.feature.catalog.data)
    implementation(projects.feature.catalog.ui)
    implementation(projects.feature.cart.api)
    implementation(projects.feature.cart.domain)
    implementation(projects.feature.cart.data)
    implementation(projects.feature.cart.ui)
    // newFeature:dependencies

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)

    testImplementation(libs.koin.test)
    testImplementation(libs.kotest.runner)
}

tasks.withType<Test>().configureEach { useJUnitPlatform() }
