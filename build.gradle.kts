import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.google.dagger.hilt.android") apply false
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
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

