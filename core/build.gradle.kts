plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.krivykh.animeapp.core"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
}