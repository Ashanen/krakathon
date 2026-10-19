plugins {
    id("krakathon.android.library")
}

dependencies {
    api(projects.core.usecase)
    api(projects.core.events)
    api(libs.kotest.runner)
    api(libs.kotest.assertions)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
}
