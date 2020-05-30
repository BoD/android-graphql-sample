plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.apollographql.apollo")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.example.graphqlsample"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val githubOauthKey = Globals.buildProperties["githubOauthKey"]
        buildConfigField("String", "GITHUB_OAUTH_KEY", "\"$githubOauthKey\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        incremental = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}

apollo {
    generateKotlinModels.set(true)
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", Versions.KOTLIN))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.ANDROIDX_NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.ANDROIDX_NAVIGATION}")
    implementation("androidx.paging:paging-runtime-ktx:2.1.2")

    // Material
    implementation("com.google.android.material:material:1.1.0")

    // Apollo
    implementation("com.apollographql.apollo:apollo-runtime:${Versions.APOLLO}")
    implementation("com.apollographql.apollo:apollo-coroutines-support:${Versions.APOLLO}")
    compileOnly("org.jetbrains:annotations:16.0.1")

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Testing
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
