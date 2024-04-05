plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}


android {
    namespace = "com.carnetdevoyageintelligent.cvi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.carnetdevoyageintelligent.cvi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    viewBinding {
        enable = true
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
    buildFeatures{
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    testImplementation(libs.junit)
    implementation(libs.material)
    implementation(libs.glide)

    //OpenStreetMap
    implementation(libs.osmdroid.android)
    implementation(libs.androidx.preference.ktx)


    // Firebase
    implementation(libs.firebase.storage)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database.ktx)

    // Androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.mediarouter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.osmdroid.android)

    implementation(libs.picasso)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.androidx.recyclerview)

}