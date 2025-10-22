@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.veterinaria"
    compileSdkVersion("android-36")
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.veterinaria"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            // Habilitar logging en builds de debug
            buildConfigField("boolean", "LOGGING_ENABLED", "true")
            // Optimizaciones para debug
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
        release {
            // Activar minificación y eliminación de recursos en release ayuda a reducir el tamaño del APK/AAB.
            // Probar en un build de staging antes de promover a producción.
            isMinifyEnabled = true
            isShrinkResources = true
            // Desactivar logging en releases por seguridad y tamaño de log
            buildConfigField("boolean", "LOGGING_ENABLED", "false")
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Crear build type intermedio para testing
        create("staging") {
            initWith(getByName("release"))
            buildConfigField("boolean", "LOGGING_ENABLED", "true")
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false  // Debe ser false si minifyEnabled es false
            applicationIdSuffix = ".staging"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        // Necesario para usar buildConfigField en buildTypes
        buildConfig = true
        // Deshabilitar features no usadas para mejorar build time
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
        viewBinding = false
        dataBinding = false
    }

    // Añadir configuración para manejar la advertencia de ashmem
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
        }
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Gson for JSON parsing
    implementation(libs.gson)

    // Kotlin Coroutines for background tasks
    implementation(libs.coroutines.android)

    // Lifecycle and ViewModel
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)

    // PDF Generation
    implementation(libs.itext7.core)
    implementation(libs.html2pdf)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    // DataStore
    implementation(libs.androidx.datastore.preferences.v100)
    // Coil optimized
    implementation(libs.coil)
    implementation(libs.coil.compose)
}

android {
    // Configuración de lint optimizada
    lint {
        checkReleaseBuilds = false
        abortOnError = false
        disable += "InvalidPackage"
    }

    // Configuración para splits de APK (opcional)
    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "arm64-v8a", "armeabi-v7a")
            isUniversalApk = false
        }
    }
}
