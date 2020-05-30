import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * Returns a file on the project's root - creates it from a sample if it doesn't exist
 */
fun Project.getOrCreateFile(fileName: String): File {
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

fun Properties.loadFromFile(file: File) {
    val fileInputStream = FileInputStream(file)
    fileInputStream.use {
        load(it)
    }
}