import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        )
    }
}

android {
    namespace = "uk.techreturners.virtuart"
    compileSdk = 36

    // Read local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    defaultConfig {
        applicationId = "uk.techreturners.virtuart"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject IP address as String resource
        resValue(
            "string",
            "wireless_ip",
            localProperties.getProperty("wireless.ip")
        )

        // Add Web Client and Backend Base URL as BuildConfig attributes
        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${localProperties.getProperty("web.client.id")}\""
        )
        buildConfigField(
            "String",
            "BASE_BACKEND_URL_WIRELESS",
            "\"${localProperties.getProperty("base.url.backend.wireless")}\""
        )

        // Generate network security config during configuration phase
        val templateFile = file("src/main/res/xml/network_security_config_template.xml")
        val targetFile = file("src/main/res/xml/network_security_config.xml")

        if (templateFile.exists() && localPropertiesFile.exists()) {
            val wirelessIp = localProperties.getProperty("wireless.ip") ?: "192.168.1.100"
            val content = templateFile.readText().replace("WIRELESS_IP_PLACEHOLDER", wirelessIp)
            targetFile.parentFile.mkdirs()
            targetFile.writeText(content)
            println("✅ Generated network_security_config.xml with IP: $wirelessIp")
        }
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    ksp {
        arg("dagger.fastInit", "enabled")
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
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
    // Glide core library
    implementation(libs.glide)
    ksp(libs.glide.compiler)

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
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Add DataStore dependency
    implementation(libs.androidx.datastore.preferences)

    // Coil
    implementation(libs.coil.compose)
}