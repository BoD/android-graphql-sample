import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.graphqlsample"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val githubOauthKey = (rootProject.ext["buildProperties"] as Properties)["githubOauthKey"]
        buildConfigField("String", "GITHUB_OAUTH_KEY", "\"$githubOauthKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlinx.coroutines.android)
    implementation(platform(libs.kotlin.bom))

    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.paging.runtime.ktx)

    // Compose
    implementation(libs.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.tooling)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.paging.compose)
    implementation(libs.navigation.compose)

    // Apollo
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.normalized.cache.sqlite)

    // Timber
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)


    // Testing
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

kapt {
    // Needed for Hilt - see https://developer.android.com/training/dependency-injection/hilt-android#kts
    correctErrorTypes = true
}

apollo {
    service("github") {
        packageName.set("com.example.graphqlsample.graphql")
        generateOptionalOperationVariables.set(false)

        introspection {
            endpointUrl.set("https://api.github.com/graphql")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
            val githubOauthKey = (rootProject.ext["buildProperties"] as Properties)["githubOauthKey"]
            headers.put("Authorization", "Bearer $githubOauthKey")
        }
    }
}

// `./gradlew downloadGithubApolloSchemaFromIntrospection` to download the schema
