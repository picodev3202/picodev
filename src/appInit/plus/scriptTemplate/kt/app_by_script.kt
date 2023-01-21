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
    for (line in file("apps_list.txt").readText().lines()) if (line.trim().run { isNotEmpty() && !startsWith("//") }) {
        val srcRootOfSimpleApp = line.trim()
        file(srcRootOfSimpleApp).apply { if (!exists()) println("'simple app' '$srcRootOfSimpleApp' not exists in :'$absolutePath'") }.takeIf { it.exists() }
            ?.run { name to parentFile.absolutePath.length }?.let { (srcRootName, relativePathHelperLength) ->
                val (codeFiles, libFiles) = listOf("code", "lib")
                    .map { place -> file("$srcRootOfSimpleApp/$place").walk().filter { it.isFile && it.extension == "kt" }.toList() }
                """${
                    (libFiles + codeFiles).joinToString("") {
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