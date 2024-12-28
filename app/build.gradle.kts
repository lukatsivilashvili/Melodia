plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("/Users/lukawivilashvili/keystores/upload-keystore.jks")
            storePassword = "Nekomamushi1."
            keyAlias = "upload"
            keyPassword = "Nekomamushi1."
        }
    }
    namespace = "ge.luka.melodia"
    compileSdk = 35

    defaultConfig {
        applicationId = "ge.luka.melodia"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Enable code shrinking and resource shrinking
            isMinifyEnabled = true
            isShrinkResources = true

            // Disable debugging for release builds
            isDebuggable = false

            // Set signing configuration for release builds
            signingConfig = signingConfigs.findByName("release")

            // Optimize ProGuard files
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default optimized ProGuard rules
                "proguard-rules.pro" // Your custom rules
            )

            // Configure advanced optimizations (if safe for your app)
            buildConfigField("boolean", "ENABLE_LOGGING", "false") // Turn off logging in release builds
            renderscriptOptimLevel = 3 // Optimize RenderScript performance

            // Remove unused code and resources
            isCrunchPngs = true // Compress PNGs during the build
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material.icons.extended)


    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose.navigation)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Datastore
    implementation(libs.preferences.datastore)

    //LifeCycle
    implementation(libs.lifecycle.viewmodel)

    // Coil
    implementation(libs.coil)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}