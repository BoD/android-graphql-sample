import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun prop(key: String) = project.findProperty(key).toString()

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.apollo)
}

android {
  namespace = "com.example.graphqlsample"
  compileSdk {
    version = release(37)
  }

  defaultConfig {
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    buildConfigField("String", "GITHUB_OAUTH_KEY", "\"${prop("githubOauthKey")}\"")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }
}

kotlin {
  jvmToolchain(21)
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
  }
}

dependencies {
  // Kotlin
  implementation(libs.kotlinx.coroutines.android)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)

  // AndroidX
  implementation(libs.appcompat)
  implementation(libs.core.ktx)
  implementation(libs.lifecycle.extensions)
  implementation(libs.lifecycle.viewmodel.ktx)

  // Compose
  implementation(libs.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.compose.ui.tooling)

  implementation(libs.lifecycle.viewmodel.compose)
  implementation(libs.paging.compose)

  // Navigation
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.hilt.navigation.compose)

  // Apollo
  implementation(libs.apollo.runtime)

  // Timber
  implementation(libs.timber)

  // Testing
  testImplementation(libs.junit.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}

apollo {
  service("github") {
    packageName.set("com.example.graphqlsample.graphql")
    generateOptionalOperationVariables.set(false)

    introspection {
      endpointUrl.set("https://api.github.com/graphql")
      schemaFile.set(file("src/main/graphql/schema.graphqls"))
      headers.put("Authorization", "Bearer ${prop("githubOauthKey")}")
    }
  }
}

// `./gradlew downloadGithubApolloSchemaFromIntrospection` to download the schema
