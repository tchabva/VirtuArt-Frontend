import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.serialization)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "uk.techreturners.virtuart"
    compileSdk = 35

    // Read local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    defaultConfig {
        applicationId = "uk.techreturners.virtuart"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject IP address as String resources
        resValue(
            "string",
            "web_client_id",
            localProperties.getProperty("web.client.id") ?: "localhost"
        )
        resValue(
            "string",
            "base_url_backend_wireless",
            localProperties.getProperty("base.url.backend.wireless")
                ?: "http://10.0.2.2:8080/api/v1/"
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    // Compose ViewModel
    implementation(libs.lifecycle.viewmodel.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Glide Compose
    implementation(libs.compose.glide)

    // Material 3 Extended Icons
    implementation(libs.androidx.material.icons.extended)

    // JDK Desugaring Libraries
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Google Auth Services
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Add DataStore dependency
    implementation(libs.androidx.datastore.preferences)
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("dagger.fastInit", "enabled")
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
        arg("dagger.hilt.android.internal.projectType", "app")
        arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
    }
}