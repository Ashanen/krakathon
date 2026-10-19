plugins {
    id("krakathon.android.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.core.exception)
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    testImplementation(libs.ktor.client.mock)
}
