import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val appId = "io.github.marcosolina.jellyfinquest"

// The release key lives in the git-ignored private/ folder. Without it (a fresh clone, CI) release builds are unsigned.
val signingFile = rootProject.file("private/signing.properties")
val signing = Properties().apply { if (signingFile.exists()) signingFile.inputStream().use(::load) }

android {
    namespace = appId
    // Recent AndroidX libraries need a recent compile SDK; the app itself targets Horizon OS, which is Android 14.
    compileSdk = 37

    defaultConfig {
        applicationId = appId
        minSdk = 34
        targetSdk = 34
        versionCode = 2
        versionName = "0.2.0"
        // Quest headsets are 64 bit ARM only, and so are the Spatial SDK's native libraries.
        ndk { abiFilters += "arm64-v8a" }
    }

    signingConfigs {
        if (signingFile.exists()) {
            create("release") {
                storeFile = rootProject.file(signing.getProperty("storeFile"))
                storePassword = signing.getProperty("storePassword")
                keyAlias = signing.getProperty("keyAlias")
                keyPassword = signing.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        // Development builds are a separate app next to the installed release, so the two never clash over signatures.
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "Jellyfin Quest (dev)")
        }
        release {
            resValue("string", "app_name", "Jellyfin Quest")
            signingConfig = signingConfigs.findByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        lintConfig = file("lint.xml")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        allWarningsAsErrors.set(true)
    }
}

dependencies {
    implementation(libs.spatial.sdk)
    implementation(libs.spatial.sdk.toolkit)
    implementation(libs.spatial.sdk.vr)
    implementation(libs.spatial.sdk.compose)
    // Interaction SDK: pointing and pinching at panels with hands as well as controllers.
    implementation(libs.spatial.sdk.isdk)

    // The window activity (ADR-003) is a plain Compose activity.
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.coroutines.android)

    constraints {
        implementation(libs.androidx.lifecycle.runtime) {
            because("Compose asks for lifecycle 2.9, whose lint checks no longer load in the current Android Lint")
        }
    }

    testImplementation(libs.junit)
}

// One command from the PC: build, install on the connected headset and start the app (JQ-013).
val debugLaunchComponent = "$appId.debug/$appId.window.WindowActivity"
val adbPath: String =
    androidComponents.sdkComponents.adb
        .get()
        .asFile.absolutePath

tasks.register<Exec>("runOnQuest") {
    group = "quest"
    description = "Builds the debug app, installs it on the connected headset and starts it."
    dependsOn("installDebug")
    // -S stops a running copy first, so the new build is the one that starts.
    commandLine(adbPath, "shell", "am", "start", "-S", "-n", debugLaunchComponent)
}
