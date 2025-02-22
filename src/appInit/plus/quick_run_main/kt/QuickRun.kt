object QuickRun {
    interface Main : By.MainWithLocalPlace

    sealed interface By {
        val objectName: String get() = this::class.java.name
        val logTagName: String get() = objectName

        interface MainWithLocalPlace : By {
            fun main(thisFile: LocalPlace, args: List<String>)
        }

        abstract class Main : By {
            class ThisFile(localPlace: LocalPlace) : LocalPlace.From(localPlace)

            abstract fun main(thisFile: ThisFile, args: List<String>)
        }
    }

    @Suppress("ConstPropertyName")
    private const val fileNameExtension_Kt = "kt"

    @Suppress("ConstPropertyName")
    private const val comment = "//"

    @Suppress("NOTHING_TO_INLINE")
    private inline fun By.main(@Suppress("unused") devProject: DevProject, thisFile: LocalPlace, args: Array<String>) = when (this) {
        is By.MainWithLocalPlace -> main(thisFile, args.toList())
        is By.Main -> main(By.Main.ThisFile(thisFile), args.toList())
    }

    fun lookupSrcFileByClassNameAndRun(mainObject: By, args: Array<String>, configure: QuickRunParams.() -> Unit) {
        val params = QuickRunParams()
        params.configure()

        val devProject = DevProjectLookup.by(args)

        val objectName = mainObject::class.java.name.let {
            val char = "$"
            if (it.contains(char)) {
                return@let it.split(char)[0]
            }
            return@let it
        }
        val expectedFileName = "${objectName}.$fileNameExtension_Kt"

        val paramsValueLocalPathToCurrentFile = params.localPathToCurrentSourceFile.value

        //@formatter:on
        val foundFiles = devProject.src.file.walk().onEnter {
            when (it.name) {
                "tmp", "out", "build", ".idea", ".git" -> {
                    System.err.println("skip walk: $it"); false
                }

                else -> true
            }
        }.filter { it.isFile && expectedFileName == it.name }.toList()

        if (foundFiles.isEmpty()) {
            TODO("file by name='$expectedFileName' not fount in '${devProject.rootStore}'")
        } else if (foundFiles.size == 1) {
            val thisFile = foundFiles[0]
            mainObject.main(devProject, LocalPlace.of(thisFile), args)
        } else {
            var allFilesValid = true
            val srcPlacePath = devProject.src.path
            val paramName = params.localPathToCurrentSourceFile.name
            for (file in foundFiles) {
                //System.err.println("QuickRunObject try to find file $file")
                val originalContent = file.readText()
                var contentWithValidParam = ""
                val foundParamList = mutableListOf<String>()
                val filePath = file.absolutePath ?: TODO()
                val fileRelativePath = if (filePath.startsWith(srcPlacePath)) filePath.substring(srcPlacePath.length).trim(LocalPlace.separatorChar)
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
                val looksLikeExpectedFile = devProject.src.place(paramsValueLocalPathToCurrentFile)
                if (looksLikeExpectedFile.exists() && looksLikeExpectedFile.name == expectedFileName) {
                    mainObject.main(devProject, looksLikeExpectedFile, args)
                    return
                }
            }
        }
    }
}