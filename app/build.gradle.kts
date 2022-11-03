import de.fayard.refreshVersions.core.versionFor
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.apollographql.apollo3")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.graphqlsample"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val githubOauthKey = (rootProject.ext["buildProperties"] as Properties)["githubOauthKey"]
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
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "_")

    // AndroidX
    implementation("androidx.appcompat", "appcompat", "_")
    implementation("androidx.core", "core-ktx", "_")
    implementation("androidx.lifecycle", "lifecycle-extensions", "_")
    implementation("androidx.lifecycle", "lifecycle-viewmodel-ktx", "_")
    implementation("androidx.paging", "paging-runtime-ktx", "_")

    // Compose
    implementation("androidx.activity", "activity-compose", "_")
    implementation("androidx.compose.material", "material", "_")
    implementation("androidx.compose.animation", "animation", "_")
    implementation("androidx.compose.ui", "ui-tooling", "_")
    implementation("androidx.lifecycle", "lifecycle-viewmodel-compose", "_")
    implementation("androidx.paging", "paging-compose", "_")
    implementation("androidx.navigation", "navigation-compose", "_")

    // Apollo
    implementation("com.apollographql.apollo3", "apollo-runtime", "_")
    implementation("com.apollographql.apollo3", "apollo-normalized-cache", "_")
    implementation("com.apollographql.apollo3", "apollo-normalized-cache-sqlite", "_")

    // Timber
    implementation("com.jakewharton.timber", "timber", "_")

    // Hilt
    implementation("com.google.dagger", "hilt-android", "_")
    kapt("com.google.dagger", "hilt-android-compiler", "_")
    implementation("androidx.hilt", "hilt-navigation-compose", "_")


    // Testing
    testImplementation("junit", "junit", "_")
    androidTestImplementation("androidx.test.ext", "junit", "_")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "_")
}

kapt {
    // Needed for Hilt - see https://developer.android.com/training/dependency-injection/hilt-android#kts
    correctErrorTypes = true
}

apollo {
    packageName.set("com.example.graphqlsample.queries")
    generateOptionalOperationVariables.set(false)

    introspection {
        endpointUrl.set("https://api.github.com/graphql")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
        val githubOauthKey = (rootProject.ext["buildProperties"] as Properties)["githubOauthKey"]
        headers.put("Authorization", "Bearer $githubOauthKey")
    }
}

// `./gradlew refreshVersions` to refresh dependencies versions
// `./gradlew downloadServiceApolloSchemaFromIntrospection` to download the schema
