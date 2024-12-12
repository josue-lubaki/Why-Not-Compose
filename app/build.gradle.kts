/*
 * Copyright 2023 Md. Mahmudul Hasan Shohag
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ------------------------------------------------------------------------
 *
 * Project: Why Not Compose!
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * Source: https://github.com/ImaginativeShohag/Why-Not-Compose
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.secrets)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "org.imaginativeworld.whynotcompose"
    compileSdk = BuildConfigConst.compileSdk

    defaultConfig {
        applicationId = "org.imaginativeworld.whynotcompose"
        minSdk = BuildConfigConst.minSdk
        targetSdk = BuildConfigConst.targetSdk
        versionCode = (findProperty("android.injected.version.code") as? String)?.toIntOrNull() ?: 1
        versionName = "7.2.0.${getCurrentDateAsYYMMDD()}" // Major.Minor.Patch.YYMMDD
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
        named("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        named("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"

        // Enable experimental compose APIs
        freeCompilerArgs =
            freeCompilerArgs + "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        freeCompilerArgs =
            freeCompilerArgs + "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        freeCompilerArgs =
            freeCompilerArgs + "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs =
            freeCompilerArgs + "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs =
            freeCompilerArgs + "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi"
    }

    buildFeatures {
        buildConfig = true
        compose = true

        // Disable unused AGP features
        viewBinding = false
        dataBinding = false

        // buildConfig false
        resValues = false
        shaders = false
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":common-ui-compose"))
    implementation(project(":tictactoe"))
    implementation(project(":exoplayer"))
    implementation(project(":cms"))
    implementation(project(":popbackstack"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.swiperefreshlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // ----------------------------------------------------------------
    // compose
    // ----------------------------------------------------------------
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    // Tooling support (Previews, etc.)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Animation
    implementation(libs.androidx.compose.animation)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    // Material Design
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    // Material design icons
    implementation(libs.androidx.compose.material.iconsCore)
    implementation(libs.androidx.compose.material.iconsExtended)
    // Integration with observables
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.runtime.tracing)
    // compose Navigation Component
    implementation(libs.androidx.navigation.compose)
    // Constraint Layout
    implementation(libs.androidx.constraintlayout.compose)
    // Integration with activities
    implementation(libs.androidx.activity.compose)

    // Jetpack compose Integration for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Paging
    implementation(libs.androidx.paging.compose)

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.swiperefresh)

    // ----------------------------------------------------------------

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)

    // Moshi
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // Gson
    implementation(libs.gson)

    // ViewModel and LiveData
    // implementation(libs.androidx.lifecycle.runtime.compose)
    // implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // implementation(libs.androidx.lifecycle.livedata.ktx)
    // implementation(libs.androidx.lifecycle.common)
    // implementation(libs.androidx.lifecycle.runtime.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room Persistence Library
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Room: Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.network)

    // Paging
    implementation(libs.androidx.paging.runtime)

    // Timber
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // No Internet Library
    implementation(libs.oopsNoInternet)

    // Maps
    implementation(libs.google.playservice.maps)
    implementation(libs.google.maps.ktx)
    implementation(libs.google.maps.utils.ktx)
    implementation(libs.google.maps.compose)

    // Lottie
    implementation(libs.lottie.compose)

    // uCrop
    implementation(libs.ucrop)

    // OneSignal
    implementation(libs.onesignal)

    // Firebase
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics.ktx)
}
