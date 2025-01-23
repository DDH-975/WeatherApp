import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(FileInputStream(localPropertiesFile))
} else {
    throw GradleException("local.properties 파일이 없거나 잘못된 위치에 있습니다.")
}


val apiKey: String = properties["Apikey"] as String? ?: throw GradleException("local.properties 파일에 Apikey가 정의되지 않았습니다.")

android {
    namespace = "com.project.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.weatherapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // API 키를 BuildConfig에 추가
        buildConfigField("String", "Apikey", "\"$apiKey\"")
    }

    buildFeatures {
        buildConfig = true  // BuildConfig 기능을 활성화
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.indicator)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
