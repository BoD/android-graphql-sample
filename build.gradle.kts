import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  alias(libs.plugins.benManes.versions)

  alias(libs.plugins.androidApplication).apply(false)
  alias(libs.plugins.kotlinAndroid).apply(false)
  alias(libs.plugins.compose.compiler).apply(false)
  alias(libs.plugins.hilt).apply(false)
  alias(libs.plugins.ksp).apply(false)
  alias(libs.plugins.apollo).apply(false)
}

tasks.withType<DependencyUpdatesTask> {
  rejectVersionIf {
    val reject = setOf("alpha", "beta", "rc", "dev")
    reject.any { candidate.version.contains("-$it", ignoreCase = true) }
  }
}

// `./gradlew dependencyUpdates` to see new dependency versions
