//
//
abstract class MainObject {
    abstract class WithThisFile : MainObject() {
        @Suppress("unused")
        class Args(val file: LocalPlace, val array: List<String>, val devProject: DevProject) {
            operator fun get(index: Int): String = array[index]
            val size = array.size
        }

        open fun main(args: Args): Unit = System.err.println(" please 'override fun main(args: Args)' in $objectName")
        override fun main(args: MainObject.Args): Unit = System.err.println(" please 'override fun main(args: Args)' in $objectName")
    }

    @Suppress("unused")
    class Args(val array: List<String>, val devProject: DevProject) {
        operator fun get(index: Int): String = array[index]
        val size = array.size
    }

    val objectName: String
        get() = this::class.java.name.let {
            val i = it.indexOf(_char)
            if (i > 0) {
                val charArray = it.toCharArray()
                charArray[i] = '.'
                return@let String(charArray)
            }
            return@let it
        }

    val logTagName: String get() = objectName
    open fun main(args: Args): Unit = System.err.println(" please 'override fun main(args: Args)' in $objectName")

    companion object {

        class QuickRunParams {
            val localPathToCurrentSourceFile by QuickNamedString()
        }

        @Suppress("ConstPropertyName")
        private const val fileNameExtension_Kt = "kt"

        @Suppress("ConstPropertyName")
        private const val comment = "//"

        @Suppress("ConstPropertyName")
        internal const val _char = "$"

        @Suppress("NOTHING_TO_INLINE")
        private inline fun MainObject.main(devProject: DevProject, thisFile: LocalPlace, args: Array<String>) {
            if (this is WithThisFile) {
                main(WithThisFile.Args(thisFile, args.toList(), devProject))
            } else {
                main(Args(args.toList(), devProject))
            }
        }

        operator fun invoke(mainObject: MainObject, args: Array<String>, configure: QuickRunParams.() -> Unit) = lookupSrcFileByClassNameAndRun(mainObject, args, configure)
        operator fun invoke(mainObject: MainObject, args: Array<String>) = lookupSrcFileByClassNameAndRun(mainObject, args) { } // TODO: correct run only with devProject

        private fun lookupSrcFileByClassNameAndRun(mainObject: MainObject, args: Array<String>, configure: QuickRunParams.() -> Unit) {
            val devProject = DevProjectLookup.by(args)
            if (mainObject is WithThisFile) {
                val params = QuickRunParams()
                params.configure()

                val objectName = mainObject::class.java.name.let {

                    if (it.contains(_char)) {
                        return@let it.split(_char)[0]
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

                val paramName = params.localPathToCurrentSourceFile.name

                if (foundFiles.isEmpty()) {
                    TODO("file by name='$expectedFileName' not fount in '${devProject.rootPlace}'")
                } else if (foundFiles.size == 1) {
                    val thisFile = LocalPlace.of(foundFiles[0])
                    val foundParamList = mutableListOf<String>()
                    val contentWithValidParam = contentWithValidParam(thisFile, paramName, foundParamList, "")
                    if (foundParamList.size == 1 && foundParamList.first().isNotEmpty()) {
                        thisFile update contentWithValidParam
                    }
                    mainObject.main(devProject, thisFile, args)
                } else {
                    var allFilesValid = true
                    val srcPlacePath = devProject.src.path
                    foundFiles.map { LocalPlace.of(it) }.forEach { file ->
                        //System.err.println("MaidObject try to find file $file")
                        val filePath = file.path
                        val fileRelativePath = if (filePath.startsWith(srcPlacePath)) filePath.substring(srcPlacePath.length).trim(LocalPlace.separatorChar)
                        else TODO(" Found file not in 'src place' $srcPlacePath")

                        val foundParamList = mutableListOf<String>()
                        val contentWithValidParam = contentWithValidParam(file, paramName, foundParamList, fileRelativePath)

                        if (foundParamList.size == 1) {
                            val fileValid = (fileRelativePath == foundParamList.first())
                            //println("debug lookupSrcFileByClassNameAndRun      $file ")
                            //println("debug lookupSrcFileByClassNameAndRun paramsValueLocalPathToCurrentFile $paramsValueLocalPathToCurrentFile ")
                            //println("debug lookupSrcFileByClassNameAndRun                                  $foundParamList ${foundParamList.size}")
                            //println("debug lookupSrcFileByClassNameAndRun                  fileRelativePath $fileRelativePath $fileValid ${originalContent == contentWithValidParam}")
                            if (!fileValid) {
                                allFilesValid = false
                                file update contentWithValidParam
                                // System.err.println("update:           file:///${file.absolutePath}")
                            }
                        } else {
                            System.err.println("MaidObject found '${foundParamList.size}' param '$paramName'  in :$file")
                        }
                    }
                    if (allFilesValid && paramsValueLocalPathToCurrentFile.isNotEmpty()) {
                        val looksLikeExpectedFile = devProject.src.place(paramsValueLocalPathToCurrentFile)
                        if (looksLikeExpectedFile.exists() && looksLikeExpectedFile.name == expectedFileName) {
                            mainObject.main(devProject, looksLikeExpectedFile, args)
                            return
                        }
                    } else {
                        System.err.println("Just found files updated, please rerun")
                    }
                }
            } else {
                mainObject.main(devProject, LocalPlace.empty, args)
            }
        }

        private fun contentWithValidParam(file: LocalPlace, paramName: String, foundParamList: MutableList<String>, fileRelativePath: String): String {
            val originalContent = file.readText()
            var contentWithValidParam = ""

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
            return contentWithValidParam
        }
    }
}