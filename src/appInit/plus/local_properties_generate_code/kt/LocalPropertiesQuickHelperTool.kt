import kotlin.reflect.KClass

object LocalPropertiesQuickHelperTool : LocalPropertiesConst by LocalPropertiesConst {

    sealed class QuickNamed<T : QuickNamed.ValCommon>(private val _val: T) {
        abstract class ValCommon {
            var name = ""

            //override fun toString() = "(name=$name, value=$value)"
            abstract fun update(content: ValCommon)
        }

        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
    }

    class QuickNamedString : QuickNamed<QuickNamedString.Val>(Val()) {
        class Val : ValCommon() {
            var value = ""
            override fun update(content: ValCommon) {
                if (content is Val) {
                    value = content.value
                }
            }

            operator fun rangeTo(value: String): Unit = run { this.value = value }
        }
    }

    class QuickNamedHighlight : QuickNamed<QuickNamedHighlight.Val>(Val()) {
        class Val : ValCommon() {
            var base = ""
            var value = emptyList<String>()

            override fun update(content: ValCommon) {
                if (content is Val) {
                    base = content.base
                    value = content.value
                }
            }

            operator fun invoke(base: String, list: List<String>): Unit = run {
                this.base = base.trim(); this.value = list
            }

            operator fun invoke(list: List<String>): Unit = run {
                this.base = this.name; this.value = list
            }
        }
    }

    class QuickNamedType(val klass: KClass<out QuickNamed<*>>) {
        val klassVal: KClass<out Any> = when (klass) {
            QuickNamedString::class -> QuickNamedString.Val::class
            QuickNamedHighlight::class -> QuickNamedHighlight.Val::class
            else -> TODO()
        }

        companion object {
            val string = QuickNamedType(QuickNamedString::class)
            val highlight = QuickNamedType(QuickNamedHighlight::class)
        }
    }

    object UpdateHelper {
        private val uid by lazy { "id --user".exec().trim() }
        private val systemTempDir by lazy { "/tmp/" }
        private val userRuntimeDirInMemory by lazy { "/run/user/$uid" }

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

            val highlights = ParamsOfHighlights()
            highlights.forEach { it.update(params.propertyByName(it.name)) }

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
            highlights.forEach {
                if (it is QuickNamedHighlight.Val) {
                    it.value.forEachIndexed { idx, path -> println("${it.name}[$idx]   is 'file:///${LocalFile(devProject.rootPlace.file(it.base), path)}'") }
                } else if (it is QuickNamedString.Val && it.value.isNotEmpty()) println("${it.name}   is 'file:///${LocalFile(devProject.srcPlace, it.value)}'")
            }
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

    private fun highlightsFileRelativePath(name: String, value: String, basePlace: LocalFile): String {
        val highlightsFile = value.trim()
        if (highlightsFile.isNotEmpty()) {
            val filePathByRelative = LocalFile(basePlace, highlightsFile)
            if (filePathByRelative.exists()) {
                println("$name 'file:///${filePathByRelative.absolutePath}'")
                return highlightsFile
            }
            val srcPlacePath = basePlace.absolutePath
            val filePath = LocalFile(highlightsFile).absolutePath
            println("$name 'file:///${filePath}'")
            val fileRelativePath = if (filePath.startsWith(srcPlacePath)) filePath.substring(srcPlacePath.length).trim(LocalFile.separatorChar)
            else TODO("value of $name is invalid '$value'")
            return fileRelativePath
        }
        return ""
    }

    private fun normalizeHelper(devProject: DevProject, highlights: ParamsOfHighlights = ParamsOfHighlights()) {
        val objectName = this::class.java.name
        val updateHelperName = UpdateHelper::class.java.name.split("$").last()
        val quickNamedStringName = QuickNamedString::class.java.name.split("$").last()
        val quickNamedStringValName = QuickNamedString.Val::class.java.name.split("$").last()
        val quickNamedName = QuickNamed::class.java.name.split("$").last()
        val quickNamedValName = QuickNamed.ValCommon::class.java.name.split("$").last()

        val paramsOfHighlightsName = ParamsOfHighlights::class.java.name
        val paramsForUpdateLocalPropertiesName = ParamsForUpdateLocalProperties::class.java.name

        class Item(val type: QuickNamedType, val name: String, val base: String = "", val valueStr: String = "", val valueList: List<String> = emptyList()) {
            fun represent(): String {
                return when (type) {
                    QuickNamedType.string -> "    ${name}..\"${highlightsFileRelativePath(name, valueStr, devProject.srcPlace)}\""
                    QuickNamedType.highlight -> "    ${name}(${if (base.isEmpty() || name == base) "" else "\"${base}\", "}listOf(\n${
                        valueList.joinToString("") {
                            "        \"${highlightsFileRelativePath(name, it, devProject.rootPlace.file(base))}\",\n"
                        }
                    }    ))"

                    else -> TODO()
                }
            }

            val typeNameRepresent: String = type.klass.java.name.split("$").last()
        }

        fun itemStr(name: String, value: String) = Item(QuickNamedType.string, name, valueStr = value)

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

            val highlightsParams = highlightsParamsOrder.map { (id, type) ->
                with(highlights.propertyByName(id)) {
                    if (this is QuickNamedHighlight.Val) {
                        Item(type, name, base = base, valueList = value)
                    } else if (this is QuickNamedString.Val) {
                        Item(type, name, valueStr = value)
                    } else TODO()
                }
            }.toTypedArray()
            val outFileRelativePath = "$thisFileParentRelativePath/$outFileName"
            val paramsNameAndOrder = listOf(
                itemStr("localPathToCurrentFile", outFileRelativePath),
                *highlightsParams,
                *localPropertiesParamsOrder.map { itemStr(it, "") }.toTypedArray()
            )

            val paramsClassStr = """@Suppress("PropertyName")
class $paramsForUpdateLocalPropertiesName {
${paramsNameAndOrder.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.${it.typeNameRepresent}()" }}

    private val map = mapOf(
${paramsNameAndOrder.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedStringName}.$quickNamedStringValName().apply { name = propertyName }
}"""

            val highlightsParamsClassStr = """@Suppress("MemberVisibilityCanBePrivate")
class $paramsOfHighlightsName {
${highlightsParams.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.${it.typeNameRepresent}()" }}

    private val map = mapOf(
${highlightsParams.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedStringName}.$quickNamedStringValName().apply { name = propertyName }

    fun forEach(action: (${objectName}.${quickNamedName}.$quickNamedValName) -> Unit): Unit = map.values.forEach(action)
}"""
            val outStr = """$comment
fun main(args: Array<String>) = ${objectName}.${updateHelperName}(args) {
    ${objectName}.highlightsParamsOrder // ... to append 'highlightSrcN'
${paramsNameAndOrder.joinToString("\n") { it.represent() }}
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

    val highlightsParamsOrder = listOf(
        "highlight" to QuickNamedType.highlight,
        "plus1" to QuickNamedType.highlight,
        "highlightSrc" to QuickNamedType.string,
        "highlightSrc1" to QuickNamedType.string,
        "highlightSrc2" to QuickNamedType.string,
        "highlightSrc3" to QuickNamedType.string,
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