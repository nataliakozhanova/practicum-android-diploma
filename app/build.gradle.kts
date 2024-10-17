
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
        minSdk = libs.versions.minSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
    implementation("io.insert-koin:koin-android:3.5.0")

    // навигация
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // фрагменты
    val fragmentVersion = "1.8.1"
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    // viewpager
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    // Materials
    implementation("com.google.android.material:material:1.12.0")
    // корутины
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // база данных
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // работа с картинками
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // сетевые запросы
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // обработка Html
    //implementation("org.sufficientlysecure:html-textview:4.0")
}
