plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.apollographql.apollo") version Versions.APOLLO
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.example.graphqlsample"
        minSdk = 24
        targetSdk = 30
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
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.1"
    }
}

apollo {
    generateKotlinModels.set(true)
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", Versions.KOTLIN))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", Versions.KOTLINX_COROUTINES)

    // AndroidX
    implementation("androidx.appcompat", "appcompat", Versions.ANDROIDX_APPCOMPAT)
    implementation("androidx.core", "core-ktx", Versions.ANDROIDX_CORE_KTX)
    implementation("androidx.constraintlayout", "constraintlayout", Versions.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation("androidx.lifecycle", "lifecycle-extensions", Versions.ANDROIDX_LIFECYCLE)
    implementation("androidx.lifecycle", "lifecycle-viewmodel-ktx", Versions.ANDROIDX_LIFECYCLE_VIEWMODEL)
    implementation("androidx.recyclerview", "recyclerview", Versions.ANDROIDX_RECYCLERVIEW)
    implementation("androidx.cardview", "cardview", Versions.ANDROIDX_CARDVIEW)
    implementation("androidx.navigation", "navigation-fragment-ktx", Versions.ANDROIDX_NAVIGATION)
    implementation("androidx.navigation", "navigation-ui-ktx", Versions.ANDROIDX_NAVIGATION)
    implementation("androidx.paging", "paging-runtime-ktx", Versions.ANDROIDX_PAGING)

    // Material
    implementation("com.google.android.material", "material", Versions.MATERIAL)

    // Compose
    implementation("androidx.activity", "activity-compose", Versions.ANDROIDX_ACTIVITY_COMPOSE)
    implementation("androidx.compose.material", "material", Versions.COMPOSE)
    implementation("androidx.compose.animation", "animation", Versions.COMPOSE)
    implementation("androidx.compose.ui", "ui-tooling", Versions.COMPOSE)
    implementation("androidx.lifecycle", "lifecycle-viewmodel-compose", Versions.ANDROIDX_LIFECYCLE_VIEWMODEL_COMPOSE)
    implementation("androidx.paging", "paging-compose", Versions.ANDROIDX_PAGING_COMPOSE)


    // Apollo
    implementation("com.apollographql.apollo", "apollo-runtime", Versions.APOLLO)
    implementation("com.apollographql.apollo", "apollo-coroutines-support", Versions.APOLLO)

    // Timber
    implementation("com.jakewharton.timber", "timber", Versions.TIMBER)

    // Testing
    testImplementation("junit", "junit", Versions.JUNIT)
    androidTestImplementation("androidx.test.ext", "junit", Versions.ANDROIDX_TEST_JUNIT)
    androidTestImplementation("androidx.test.espresso", "espresso-core", Versions.ANDROIDX_TEST_ESPRESSO)
}
