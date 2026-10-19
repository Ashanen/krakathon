plugins {
    id("krakathon.android.library")
}

dependencies {
    api(projects.core.exception)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.kotlinx.coroutines.core)
}
