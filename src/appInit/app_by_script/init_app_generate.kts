import java.io.File

File("").absoluteFile.parentFile.apply { if (!exists()) println("'simple app' '$name' not exists in :'$absolutePath'") }.takeIf { it.exists() }
    ?.let { srcRoot ->
        val relativePathHelperLength = srcRoot.parentFile.absolutePath.length
        val (codeFiles, libFiles) = listOf("code", "lib").map { place -> File(srcRoot, place).walk().filter { it.isFile && it.extension == "kt" }.toList() }
        """${
            (libFiles + codeFiles).joinToString("") {
                val relativePath = it.absolutePath.substring(relativePathHelperLength).trimStart(java.io.File.separatorChar)
                "// begin $relativePath\n${it.readText()}\n//  end  $relativePath\n"
            }
        }${
            codeFiles.joinToString("\n") {
                val objectName = it.nameWithoutExtension
                if (objectName.first().isLowerCase()) """${objectName}.main(arrayOf("${it.absolutePath}"))""" else ""
            }
        }
""".trim().takeIf { it.isNotEmpty() }?.let { generatedScriptText ->
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
    }
