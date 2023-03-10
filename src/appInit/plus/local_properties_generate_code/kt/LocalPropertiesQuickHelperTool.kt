object LocalPropertiesQuickHelperTool : LocalPropertiesConst by LocalPropertiesConst {
    class QuickNamedString {
        class Val {
            var name = ""
            var value = ""
            operator fun rangeTo(value: String): Unit = run { this.value = value }
            override fun toString() = "(name=$name, value=$value)"
        }

        private val _val = Val()
        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
    }

    object UpdateHelper {
        private val uid by lazy { "id --user".exec().trim() }
        private val systemTempDir by lazy { "/tmp/" }
        private val userRuntimeDirInMemory by lazy { "/var/run/user/$uid" }

        operator fun invoke(args: Array<String>, configure: ParamsForUpdateLocalProperties.() -> Unit) {
            val params = ParamsForUpdateLocalProperties()
            params.configure()

            val devProject = args.firstOrNull()?.let { LocalFile(it) }
                ?.let { if (it.exists()) DevProject(RootPlace.lookupToParentOf(it)) else null }
                ?: DevProject.lookupFromCurrentDir()

            val localPropertiesPlace = devProject.rootPlace.file(DevProject.localPropertiesPlace)

            println("LocalPropertiesHelper   rootPlace ${devProject.rootPlace.file}")
            println("LocalPropertiesHelper update from ${params.localPathToCurrentFile}")

            val scratches = "/scratches/"
            val consolesIde = "consoles/ide"
            val defaultIdeScript = "ide-scripting.kts"
            val ideScripting = "$consolesIde/$defaultIdeScript"
            params.params_pathForLookupIdeScriptingPlace.value.trim().let {
                if (it.isNotBlank()) {

                    val ideConfDir = LocalFile(
                        if (it.contains(scratches)) it.split(scratches)[0]
                        else if (it.contains(consolesIde)) it.split(consolesIde)[0]
                        else TODO("unexpected path for lookupIdeScriptingPlace '$it'")
                    )
                    if (ideConfDir.exists()) {
                        val ideScriptingFile = LocalFile(ideConfDir, ideScripting)
                        ideScriptingFile create {
                            """//
KotlinVersion.CURRENT
"""
                        }
                        val consolesIdeDir = LocalFile(ideConfDir, consolesIde).absoluteFile
                        if (consolesIdeDir.exists()) {
                            val pathOfIdeScripting = LocalFile(localPropertiesPlace, conf_place.of_ide_scripting)
                            println("UpdateHelper.invoke ${consolesIdeDir}")
                            pathOfIdeScripting update consolesIdeDir.absolutePath
                        } else TODO()
                    } else TODO()
                }
            }

            if (uid.isEmpty()) TODO()

            val confTmpPlace = fun(it: QuickNamedString.Val, confPlace: String, sysPlace: String, dirName: (String) -> String) {
                val prefix = it.value.trim().ifEmpty { "one" }
                val pathOfTmpConf = LocalFile(localPropertiesPlace, confPlace)
                if (!LocalFile(sysPlace).exists()) TODO("'$sysPlace' not exists")
                val tmpDirNewPath = "$sysPlace/${dirName(prefix)}"
                val tmpDirNew = LocalFile(tmpDirNewPath).absoluteFile
                if (tmpDirNew.exists()) TODO("'$tmpDirNew' already exists, please use another prefix for '${it.name}'")
                pathOfTmpConf update tmpDirNew.absolutePath
                println("UpdateHelper confTmpPlace ${tmpDirNew.absolutePath}")
            }

            val highlightsFileRelativePath = fun(it: QuickNamedString.Val): String {
                val highlightsFile = it.value.trim()
                if (highlightsFile.isNotEmpty()) {
                    val filePathByRelative = LocalFile(devProject.srcPlace, highlightsFile)
                    if (filePathByRelative.exists()) {
                        println("${it.name} 'file:///${filePathByRelative.absolutePath}'")
                        return highlightsFile
                    }
                    val srcPlacePath = devProject.srcPlace.absolutePath
                    val filePath = LocalFile(highlightsFile).absolutePath
                    println("${it.name} 'file:///${filePath}'")
                    @Suppress("UnnecessaryVariable")
                    val fileRelativePath = if (filePath.startsWith(srcPlacePath)) filePath.substring(srcPlacePath.length).trim(LocalFile.separatorChar)
                    else TODO("value of ${it.name} is invalid '${it.value}'")
                    return fileRelativePath
                }
                return ""
            }
            val highlights = ParamsOfHighlights()
            highlights.forEach { it.value = highlightsFileRelativePath(params.propertyByName(it.name)) }

            confTmpPlace(params.params_prefixOfTmpDirQuick, conf_place.of__tmp_dir_quick, userRuntimeDirInMemory) { "${devProject.name}_$it" }
            confTmpPlace(params.params_prefixOfTmpDirBig, conf_place.of__tmp_dir_big, systemTempDir) { "${devProject.name}_${uid}_$it" }

            println()
            normalizeHelper(devProject, highlights)
            println()
//            println("value of 'pathOfTmpDirBig'    is '${devProject.localProperties.pathOfTmpDirBig}'")
//            println("value of 'pathOfTmpDirQuick'  is '${devProject.localProperties.pathOfTmpDirQuick}'")
//            println("value of 'pathOfIdeScripting' is '${devProject.localProperties.pathOfIdeScripting}'")
            println("value of 'pathOfTmpDirBig'    is '${devProject.extPlace.tmpDirBig}'")
            println("value of 'pathOfTmpDirQuick'  is '${devProject.extPlace.tmpDirQuick}'")
            //println("value of 'pathOfIdeScripting' is '${devProject.extPlace.ideScripting}'")


            if (devProject.localProperties.pathOfIdeScripting.isEmpty()) println("path to 'ide scripting' is undefined")
            else println("value of 'default ide script' is 'file:///${LocalFile(devProject.extPlace.ideScripting, defaultIdeScript)}'")
            highlights.forEach { if (it.value.isNotEmpty()) println("${it.name}   is 'file:///${LocalFile(devProject.srcPlace, it.value)}'") }
        }
    }

    fun String.exec(): String {
        val process = ProcessBuilder("sh", "-c", this).start()
        var out = ""
        java.io.BufferedReader(java.io.InputStreamReader(process.inputStream)).use { input ->
            var line: String?
            while (input.readLine().also { line = it } != null) {
                out += line
                out += "\n"
            }
        }
        process.destroy()
        return out
    }

    @Suppress("NOTHING_TO_INLINE")
    inline infix fun java.io.File.update(text: String): Unit = if (!exists()) create { text } else if (readText() != text) {
        parentFile.mkdirs()
        writeText(text)
        println("update:           file:///$absolutePath")
    } else println("content the same: file:///$absolutePath")

    inline infix fun java.io.File.create(getText: () -> String): Unit = if (!exists()) {
        val str = getText()
        if (str.isNotEmpty()) {
            parentFile.mkdirs()
            writeText(str)
            println("create:           file:///$absolutePath")
        } else println("nothing to write: file:///$absolutePath")
    } else println("already exists:   file:///$absolutePath")

    private fun normalizeHelper(devProject: DevProject, highlights: ParamsOfHighlights = ParamsOfHighlights()) {
        val objectName = this::class.java.name
        val updateHelperName = UpdateHelper::class.java.name.split("$").last()
        val quickNamedStringName = QuickNamedString::class.java.name.split("$").last()
        val quickNamedStringValName = QuickNamedString.Val::class.java.name.split("$").last()
        val paramsOfHighlightsName = ParamsOfHighlights::class.java.name
        val paramsForUpdateLocalPropertiesName = ParamsForUpdateLocalProperties::class.java.name

        class Item(val name: String, val value: String)

        //@formatter:on
        val foundFiles = devProject.srcPlace.walk().onEnter {
            when (it.name) {
                "build", ".idea", ".git" -> {
                    println("skip walk: $it"); false
                }

                else -> true
            }
        }.filter { it.isFile && objectName == /*it.name &&*/ it.nameWithoutExtension }.toList()

        if (foundFiles.size == 1) {
            val thisFile = foundFiles[0]
            val srcPlacePath = devProject.srcPlace.absolutePath
            val thisFileParentPath = thisFile.absoluteFile.parentFile?.absolutePath ?: TODO()
            val thisFileParentRelativePath = if (thisFileParentPath.startsWith(srcPlacePath)) thisFileParentPath.substring(srcPlacePath.length).let {
                if (it.startsWith(LocalFile.separatorChar)) it.substring(1) else it
            } else TODO()

            val highlightsParams = highlightsParamsOrder.map { with(highlights.propertyByName(it)) { Item(name, value) } }.toTypedArray()
            val outFileRelativePath = "$thisFileParentRelativePath/$outFileName"
            val paramsNameAndOrder = listOf(
                Item("localPathToCurrentFile", outFileRelativePath),
                *highlightsParams,
                *localPropertiesParamsOrder.map { Item(it, "") }.toTypedArray()
            )

            val paramsClassStr = """@Suppress("PropertyName")
class $paramsForUpdateLocalPropertiesName {
${paramsNameAndOrder.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.$quickNamedStringName()" }}

    private val map = mapOf(
${paramsNameAndOrder.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedStringName}.$quickNamedStringValName().apply { name = propertyName; value = "" }
}"""

            val highlightsParamsClassStr = """@Suppress("MemberVisibilityCanBePrivate")
class $paramsOfHighlightsName {
${highlightsParams.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.$quickNamedStringName()" }}

    private val map = mapOf(
${highlightsParams.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedStringName}.$quickNamedStringValName().apply { name = propertyName; value = "" }

    fun forEach(action: (${objectName}.${quickNamedStringName}.$quickNamedStringValName) -> Unit): Unit = map.values.forEach(action)
}"""
            val outStr = """$comment
fun main(args: Array<String>) = ${objectName}.${updateHelperName}(args) {
${paramsNameAndOrder.joinToString("\n") { "    ${it.name}..\"${it.value}\"" }}
}"""

            LocalFile(thisFileParentPath, "${paramsForUpdateLocalPropertiesName}.$fileNameExtension_Kt") update paramsClassStr
            LocalFile(thisFileParentPath, "${paramsOfHighlightsName}.$fileNameExtension_Kt") update highlightsParamsClassStr
            LocalFile(thisFileParentPath, outFileName) update outStr
        } else {
            foundFiles.forEach { println("'$objectName' foundFile = $it") }
            TODO("found ${objectName}.$fileNameExtension_Kt files count = ${foundFiles.size}")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //val rootPlace = RootPlace.lookupFromCurrentDir()
        val devProject = DevProject.lookupFromCurrentDir()
        normalizeHelper(devProject)
    }

    private val highlightsParamsOrder = listOf(
        "highlightSrc",
        "highlightSrc1",
        "highlightSrc2",
    )
    private const val paramPrefix = "params_"
    private val tmpFolderParamsOrder = arrayOf(
        "${paramPrefix}prefixOfTmpDirBig",
        "${paramPrefix}prefixOfTmpDirQuick",
    )
    private const val paramNamePathForLookupIdeScriptingPlace = "${paramPrefix}pathForLookupIdeScriptingPlace"
    private val localPropertiesParamsOrder = listOf(
        *tmpFolderParamsOrder,
        paramNamePathForLookupIdeScriptingPlace
    )
    private const val fileNameExtension_Kt = "kt"
    private const val comment = "//"
    private const val outFileName = "fff.$fileNameExtension_Kt"
}