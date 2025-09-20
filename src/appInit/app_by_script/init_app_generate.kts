import java.io.File

val currentDir: File = File("").absoluteFile
println("currentDir is : $currentDir")
val srcRoot: File? = currentDir.parentFile
if (srcRoot != null && srcRoot.exists()) {
        val relativePathHelperLength = srcRoot.parentFile.absolutePath.length
    val (codeFiles: List<File>, libFiles: List<File>) = listOf("code", "lib")
        .map { place -> File(srcRoot, place).walk().filter { it.isFile && it.extension == "kt" }.toList() }

    val filesContent: String = (libFiles + codeFiles).joinToString("") {
        val relativePath = it.absolutePath.substring(relativePathHelperLength).trimStart(File.separatorChar)
                "// begin $relativePath\n${it.readText()}\n//  end  $relativePath\n"
            }

    val mainObjectsCall: String = codeFiles.joinToString("\n") {
                val objectName = it.nameWithoutExtension
                if (objectName.first().isLowerCase()) """${objectName}.main(arrayOf("${it.absolutePath}"))""" else ""
            }

    """${filesContent}${mainObjectsCall}""".trim().takeIf { it.isNotEmpty() }?.let { generatedScriptText ->
            File(srcRoot.parentFile.parentFile, "wwgen/tmp/generatedInitScript.kts").run {
                if (exists() && readText() == generatedScriptText) {
                    println("no need to update file :$absolutePath")
                } else {
                    if (exists()) {
                        println("           update file :$absolutePath")
                    } else {
                        parentFile.mkdirs()
                        println("          create  file :$absolutePath")
                    }
                    writeText(generatedScriptText)
                }
            }
        }
} else {
    println("'simple app' '${srcRoot?.name}' not exists in (current dir) :'$${srcRoot?.absolutePath}'")
}
