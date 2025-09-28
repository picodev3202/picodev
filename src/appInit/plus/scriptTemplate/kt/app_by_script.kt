@file:Suppress("unused")

val tmp01 = ScriptApi.
//
//
//
//
//
//
//
//
buildscript {
    operator fun <T> List<List<T>>.component5() = takeIf { it.size >= 5 }?.let { it[4] } ?: TODO()
    operator fun <T> List<List<T>>.component6() = takeIf { it.size >= 6 }?.let { it[5] } ?: TODO()
    operator fun <T> List<List<T>>.component7() = takeIf { it.size >= 7 }?.let { it[6] } ?: TODO()
    operator fun <T> List<List<T>>.component8() = takeIf { it.size >= 8 }?.let { it[7] } ?: TODO()

    for (line in file("apps_list.txt").readText().lines()) if (line.trim().run { isNotEmpty() && !startsWith("//") }) {
        val srcRootOfSimpleApp = line.trim()
        file(srcRootOfSimpleApp).apply { if (!exists()) println("'simple app' '$srcRootOfSimpleApp' not exists in :'$absolutePath'") }.takeIf { it.exists() }
            ?.run { name to parentFile.absolutePath.length }?.let { (srcRootName, relativePathHelperLength) ->

                val (codeFiles, codeConfFiles, codeModuleFiles, codePlusFiles, srcLibFiles, libFiles) =
                    listOf("code", "codeLib/codeConf", "codeLib/codeModule", "codeLib/codePlus", "codeLib/srcLib", "lib")

                        .map { place -> file("$srcRootOfSimpleApp/$place").walk().filter { it.isFile && it.extension == "kt" }.toList() }

                val allFoundFiles = libFiles + srcLibFiles + codePlusFiles + codeConfFiles + codeModuleFiles + codeFiles

                """${
                    allFoundFiles.joinToString("") {
                        val relativePath = it.absolutePath.substring(relativePathHelperLength).trimStart(java.io.File.separatorChar)
                        "// begin $relativePath\n${it.readText()}\n//  end  $relativePath\n"
                    }
                }${
                    codeFiles.joinToString("\n") {
                        val objectName = it.nameWithoutExtension
                        val taskTame = "${srcRootName}__cmd_$objectName"
                        if (objectName.first().isLowerCase()) """tasks.register("$taskTame"){ description = "from '$srcRootOfSimpleApp'"; group = "scripts"; doLast { ${objectName}.main(arrayOf("${it.absolutePath}")) } }""" else ""
                    }
                }
""".trim().takeIf { it.isNotEmpty() }?.let { generatedScriptText ->
                    apply(from = ".gradle/_${srcRootName}_generated.gradle.kts".also {
                        file(it).run {
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
                    })
                }
            } ?: run { println("'simple app' '$srcRootOfSimpleApp' looks like empty") }
    }
}