plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.qrg"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qrg"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../app/keystore/my-release-key.jks")
            storePassword = project.findProperty("KEYSTORE_PASSWORD")?.toString() ?: ""
            keyAlias = "my-key-alias"
            keyPassword = project.findProperty("KEY_PASSWORD")?.toString() ?: ""
        }
    }

    buildTypes {
        release {
            getByName("release") {
                signingConfig = signingConfigs.getByName("release")
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        viewBinding = true
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

    implementation(libs.zxing.android.embedded)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.multidex)

    //sdp
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

}