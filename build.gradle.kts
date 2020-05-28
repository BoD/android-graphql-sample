import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.FileInputStream
import java.util.*

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

// Run `./gradlew dependencyUpdates` to see latest versions of dependencies
tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = Versions.GRADLE
    }

    // Configuration for gradle-versions-plugin
    withType<DependencyUpdatesTask> {
        resolutionStrategy {
            componentSelection {
                all {
                    if (setOf(
                            "alpha",
                            "beta",
                            "rc",
                            "preview",
                            "eap",
                            "m1"
                        ).any { candidate.version.contains(it, true) }
                    ) {
                        reject("Non stable")
                    }
                }
            }
        }
    }
}

// Returns a file on the project's root - creates it from a sample if it doesn't exist
fun getOrCreateFile(fileName: String): File {
    val res = file(fileName)
    if (!res.exists()) {
        logger.warn("$fileName file does not exist: creating it now - please check its values")
        copy {
            from("${fileName}.SAMPLE")
            into(project.projectDir)
            rename { fileName }
        }
    }
    return res
}
// Build properties
val buildProperties = Properties()
Globals.buildProperties = buildProperties
val buildPropertiesFile = getOrCreateFile("build.properties")
buildProperties.load(FileInputStream(buildPropertiesFile))
