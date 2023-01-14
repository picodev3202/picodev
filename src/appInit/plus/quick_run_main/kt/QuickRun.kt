object QuickRun {
    interface Main {
        fun main(thisFile: LocalFile, args: List<String>)
    }

    val fileNameExtension_Kt = "kt"
    val comment = "//"

    fun lookupSrcFileByClassNameAndRun(mainObject: Main, args: Array<String>, configure: QuickRunParams.() -> Unit) {
        val params = QuickRunParams()
        params.configure()

        val devProject = args.firstOrNull()?.let { LocalFile(it) }
            ?.let { if (it.exists()) DevProject(RootPlace.lookupToParentOf(it)) else null }
            ?: DevProject.lookupFromCurrentDir()

        val objectName = mainObject::class.java.name
        val expectedFileName = "${objectName}.$fileNameExtension_Kt"

        val paramsValueLocalPathToCurrentFile = params.localPathToCurrentSourceFile.value

        //@formatter:on
        val foundFiles = devProject.srcPlace.walk().onEnter {
            when (it.name) {
                "tmp", "out", "build", ".idea", ".git" -> {
                    System.err.println("skip walk: $it"); false
                }

                else -> true
            }
        }.filter { it.isFile && expectedFileName == it.name }.toList()

        if (foundFiles.isEmpty()) {
            TODO("file by name='$expectedFileName' not fount in '${devProject.rootPlace}'")
        } else if (foundFiles.size == 1) {
            val thisFile = foundFiles[0]
            mainObject.main(thisFile, args.toList())
        } else {
            var allFilesValid = true
            val srcPlacePath = devProject.srcPlace.absolutePath
            val paramName = params.localPathToCurrentSourceFile.name
            for (file in foundFiles) {
                //System.err.println("QuickRunObject try to find file $file")
                val originalContent = file.readText()
                var contentWithValidParam = ""
                val foundParamList = mutableListOf<String>()
                val filePath = file.absolutePath ?: TODO()
                val fileRelativePath = if (filePath.startsWith(srcPlacePath)) filePath.substring(srcPlacePath.length).trim(LocalFile.separatorChar)
                else TODO(" Found file not in 'src place' $srcPlacePath")

                for (line in originalContent.lines()) {
                    contentWithValidParam += if (!line.trimStart().startsWith(comment) && line.contains(paramName)) {
                        val arr = line.split(paramName)
                        val foundParamValue = arr[1].trim().trimStart('.').trimStart().trim('"')
                        foundParamList.add(foundParamValue)
                        val newLine = arr[0] + paramName + "..\"$fileRelativePath\""
                        newLine
                    } else {
                        line
                    }
                    contentWithValidParam += "\n"
                }

                contentWithValidParam = contentWithValidParam.trim()
                if (foundParamList.size == 1) {
                    val fileValid = (fileRelativePath == foundParamList.first())
                    //println("debug lookupSrcFileByClassNameAndRun      $file ")
                    //println("debug lookupSrcFileByClassNameAndRun paramsValueLocalPathToCurrentFile $paramsValueLocalPathToCurrentFile ")
                    //println("debug lookupSrcFileByClassNameAndRun                                  $foundParamList ${foundParamList.size}")
                    //println("debug lookupSrcFileByClassNameAndRun                  fileRelativePath $fileRelativePath $fileValid ${originalContent == contentWithValidParam}")
                    if (!fileValid) {
                        allFilesValid = false
                        file.writeText(contentWithValidParam)
                        System.err.println("update:           file:///${file.absolutePath}")
                    }
                } else {
                    System.err.println("QuickRunObject found '${foundParamList.size}' param '$paramName'  in :$file")
                }
            }
            if (allFilesValid && paramsValueLocalPathToCurrentFile.isNotEmpty()) {
                val looksLikeExpectedFile = LocalFile(devProject.srcPlace, paramsValueLocalPathToCurrentFile)
                if (looksLikeExpectedFile.exists() && looksLikeExpectedFile.name == expectedFileName) {
                    mainObject.main(looksLikeExpectedFile, args.toList())
                    return
                }
            }
        }
    }
}