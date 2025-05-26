
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material) // 중복된 material 의존성 제거
//    implementation(libs.androidx.constraintlayout)
//    implementation(libs.androidx.lifecycle.livedata.ktx)
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.androidx.navigation.fragment.ktx)
//    implementation(libs.androidx.navigation.ui.ktx)
//    implementation("androidx.viewpager2:viewpager2:1.0.0") // ViewPager2 추가
//    implementation("me.relex:circleindicator:2.1.6")
//    implementation("androidx.appcompat:appcompat:1.7.0")
//    implementation("com.google.android.material:material:1.12.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
//    implementation(("com.google.code.gson:gson:2.10.1"))
//    implementation(libs.firebase.database)
//    testImplementation("androidx.test.ext:junit:1.2.1")
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//
//    implementation(androidx.appcompat:appcompat::1.4.0)
//    implementation(com.google.android.material:material:1.4.0)
//    implementation(androidx.constraintlayout::constraintlayout::2.1.2)
//    testImplementation(junit:junit:4.+)
//    //Retrofit
//    implementation "com.squareup.retrofit2:retrofit:2.9.0"
//    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
//    implementation "com.squareup.retrofit2:adapter-rxjava2:2.9.0"
//
//    //okHttp
//    implementation "com.squareup.okhttp3:okhttp:4.9.0"
//    implementation "com.squareup.okhttp3:logging-interceptor:4.9.0"
//
//    //Glide
//    implementation 'com.github.bumptech.glide:glide:4.11.0'
//    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
//
//
//    //RoomDB
//    implementation("androidx.room:room-ktx:2.7.1")
//    implementation("androidx.room:room-runtime:2.7.1")
//    kapt("androidx.room:room-compiler:2.7.1")
//
//    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
//    implementation("com.google.firebase:firebase-analytics")

    dependencies {
        // AndroidX
        implementation(libs.androidx.core.ktx)
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.constraintlayout:constraintlayout:2.2.1")
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation("androidx.viewpager2:viewpager2:1.0.0")

        // Material
        implementation("com.google.android.material:material:1.12.0")

        // Navigation
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
        implementation("com.google.firebase:firebase-analytics")
        implementation(libs.firebase.database)

        // Gson
        implementation("com.google.code.gson:gson:2.10.1")

        // CircleIndicator (ViewPager indicator)
        implementation("me.relex:circleindicator:2.1.6")

        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

        // OkHttp
        implementation("com.squareup.okhttp3:okhttp:4.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

        // Glide
        implementation("com.github.bumptech.glide:glide:4.11.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

        // Room DB
        implementation("androidx.room:room-ktx:2.7.1")
        implementation("androidx.room:room-runtime:2.7.1")
        kapt("androidx.room:room-compiler:2.7.1")

        // Testing
        testImplementation(libs.junit)
        testImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }

}

