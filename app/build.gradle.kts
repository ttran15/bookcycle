plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android") // Replaced alias with id
}

android {
    namespace = "com.example.bookcycle"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bookcycle"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.google.firebase:firebase-firestore:24.0.1")
    implementation ("com.google.firebase:firebase-storage:20.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx") // Added Firestore dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.firebase.database.ktx)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // Ensure only one version is used
    implementation("androidx.core:core-ktx:1.10.1") // Updated to use the latest stable version
    implementation("androidx.appcompat:appcompat:1.6.1") // Updated to use the latest stable version
    implementation("com.google.android.material:material:1.9.0") // Updated to use the latest stable version
    implementation("androidx.activity:activity-ktx:1.7.2") // Updated to use the latest stable version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Updated to use the latest stable version
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Updated to use the latest stable version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Updated to use the latest stable version
}
