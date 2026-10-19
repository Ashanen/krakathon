plugins {
    id("krakathon.android.library")
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}
