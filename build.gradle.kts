import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.benManes.versions)

    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.apollo) apply false
}


// Build properties
ext["buildProperties"] = loadPropertiesFromFile("build.properties")
fun Project.loadPropertiesFromFile(fileName: String): Properties {
    val file = file(fileName)
    if (!file.exists()) {
        logger.warn("$fileName file does not exist: creating it now - please check its values")
        copy {
            from("${fileName}.SAMPLE")
            into(project.projectDir)
            rename { fileName }
        }
    }
    val res = Properties()
    val fileInputStream = FileInputStream(file)
    fileInputStream.use {
        res.load(it)
    }
    return res
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        val reject = setOf("alpha", "beta", "rc", "dev")
        reject.any { candidate.version.contains("-$it", ignoreCase = true) }
    }
}

// `./gradlew dependencyUpdates` to see new dependency versions
