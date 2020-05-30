import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version Versions.BEN_MANES_VERSIONS_PLUGIN
}

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath(kotlin("gradle-plugin", Versions.KOTLIN))
        classpath("com.apollographql.apollo:apollo-gradle-plugin:${Versions.APOLLO}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.ANDROIDX_NAVIGATION}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.BEN_MANES_VERSIONS_PLUGIN}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = Versions.GRADLE
    }

    // Configuration for gradle-versions-plugin
    // Run `./gradlew dependencyUpdates` to see latest versions of dependencies
    withType<DependencyUpdatesTask> {
        resolutionStrategy {
            componentSelection {
                all {
                    if (setOf("alpha", "beta", "rc", "preview", "eap", "m1").any { candidate.version.contains(it, true) }) {
                        reject("Non stable")
                    }
                }
            }
        }
    }
}

// Build properties
Globals.buildProperties.loadFromFile(getOrCreateFile("build.properties"))
