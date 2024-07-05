import com.android.sdklib.AndroidVersion

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-kapt")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = AndroidVersion.VersionCodes.O
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "HH_ACCESS_TOKEN", value = "\"${developProperties.hhAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion

    // запрос разрешений
    implementation("com.markodevcic:peko:3.0.5")

    // DI зависимости
    implementation("io.insert-koin:koin-android:3.3.0")

    // навигация
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // фрагменты
    val fragmentVersion = "1.5.6"
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    // viewpager
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // Materials
    implementation("com.google.android.material:material:1.11.0")
    // корутины
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // база данных
    val roomVersion = "2.5.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // работа с картинками
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // сетевые запросы
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.0.0")
}
