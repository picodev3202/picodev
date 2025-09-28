import java.io.File

class Code {
    companion object {
        operator fun <T> List<List<T>>.component5() = takeIf { it.size >= 5 }?.let { it[4] } ?: TODO()
        operator fun <T> List<List<T>>.component6() = takeIf { it.size >= 6 }?.let { it[5] } ?: TODO()
        operator fun <T> List<List<T>>.component7() = takeIf { it.size >= 7 }?.let { it[6] } ?: TODO()
        operator fun <T> List<List<T>>.component8() = takeIf { it.size >= 8 }?.let { it[7] } ?: TODO()

        fun readAndGenerate(args: Array<String>) {
            val currentDir: File = File("").absoluteFile
            println("currentDir is : $currentDir")

            @Suppress("MoveLambdaOutsideParentheses")
            val generatedScriptName = args.getOrElse(0, { "init_app_generated_script" }).trim()

            @Suppress("MoveLambdaOutsideParentheses")
            val objectNameToRun = args.getOrElse(1, { "" }).trim()

            val srcRoot: File? = currentDir.parentFile
            if (srcRoot != null && srcRoot.exists()) {
                val relativePathHelperLength = srcRoot.parentFile.absolutePath.length

                val (codeFiles, codeConfFiles, codeModuleFiles, codePlusFiles, srcLibFiles, libFiles) =
                    listOf("code", "codeLib/codeConf", "codeLib/codeModule", "codeLib/codePlus", "codeLib/srcLib", "lib")

                        .map { place -> File(srcRoot, place).walk().filter { it.isFile && it.extension == "kt" }.toList() }

                val allFoundFiles = libFiles + srcLibFiles + codePlusFiles + codeConfFiles + codeModuleFiles + codeFiles

                val filesContent: String = allFoundFiles.joinToString("") {
                    val relativePath = it.absolutePath.substring(relativePathHelperLength).trimStart(File.separatorChar)
                    "// begin $relativePath\n${it.readText()}\n//  end  $relativePath\n"
                }

                var needObjectToRun = true
                val mainObjectsCall: String = codeFiles.joinToString("\n") {
                    val objectName = it.nameWithoutExtension
                    if (needObjectToRun &&
                        (objectNameToRun.isNotEmpty() && objectNameToRun == objectName)
                        || (objectNameToRun.isEmpty() && objectName.first().isLowerCase())
                    ) {
                        needObjectToRun = false
                        """${objectName}.main(arrayOf("${it.absolutePath}"))"""
                    } else {
                        """// ${objectName}.main(arrayOf("${it.absolutePath}"))"""
                    }
                }

                """${filesContent}${mainObjectsCall}""".trim().takeIf { it.isNotEmpty() }?.let { generatedScriptText ->
                    @Suppress("SpellCheckingInspection")
                    File(srcRoot.parentFile.parentFile, "wwgen/tmp/generated_script/${generatedScriptName}.kts").run {
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
        }
    }

    fun start() = readAndGenerate(args)
}

Code().start()
