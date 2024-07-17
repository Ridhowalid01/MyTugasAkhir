plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
//    id("com.chaquo.python")
}

android {
    namespace = "com.example.mytugasakhir"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.mytugasakhir"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        ndk {
//            abiFilters += listOf("arm64-v8a", "x86_64")
//        }

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
//    flavorDimensions += "3.8"
//    productFlavors {
//        create("py38") { dimension = "3.8" }
//    }
}

//chaquopy {
//    defaultConfig {
//        version = "3.8"
//        buildPython("C:/Users/ASUS/AppData/Local/Programs/Python/Python38/python.exe")
//
//        pip {
//
//            install("noisereduce==2.0.1")
//            install("fastdtw==0.3.4")
//            install("numpy==1.19.5")
//            install("scipy==1.4.1")
//            install("resampy==0.2.2")
//            install("librosa==0.9.2")
//            install("torch==1.4.0")
//
//        }
//    }
//    productFlavors {
//        getByName("py38") { version = "3.8" }
//    }
//    sourceSets { }
//}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.documentfile:documentfile:1.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // API
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation ("com.github.squti:Android-Wave-Recorder:1.7.0")

}