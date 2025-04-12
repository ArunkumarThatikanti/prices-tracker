plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.real_timepricestracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.real_timepricestracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Biometric
    implementation("androidx.biometric:biometric:1.1.0")

// Firebase Messaging
    implementation("com.google.firebase:firebase-messaging:23.4.1")

// MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

// Material Design
    implementation("com.google.android.material:material:1.9.0")

// ViewModel & LiveData (optional)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")

}