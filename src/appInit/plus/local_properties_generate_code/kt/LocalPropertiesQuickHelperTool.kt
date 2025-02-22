import kotlin.reflect.KClass

@Suppress("ConstPropertyName")
object LocalPropertiesQuickHelperTool : LocalPropertiesConst by LocalPropertiesConst {

    const val EMPTY_STR_VALUE = ""

    sealed class QuickNamed<T : QuickNamed.ValCommon>(private val _val: T) {
        abstract class ValCommon {
            var name = EMPTY_STR_VALUE

            //override fun toString() = "(name=$name, value=$value)"
            abstract fun update(content: ValCommon)
        }

        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
    }

    class QuickNamedEmpty : QuickNamed<QuickNamedEmpty.Val>(Val()) {
        class Val : ValCommon() {
            var value = EMPTY_STR_VALUE
            override fun update(content: ValCommon) {
            }
        }
    }

    class QuickNamedString : QuickNamed<QuickNamedString.Val>(Val()) {
        class Val : ValCommon() {
            var value = EMPTY_STR_VALUE
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
            var base = EMPTY_STR_VALUE
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
            val highlightInSrc = QuickNamedType(QuickNamedString::class)
            val highlights = QuickNamedType(QuickNamedHighlight::class)
            val highlightsUser = QuickNamedType(QuickNamedHighlight::class)
        }
    }

    object UpdateHelper {
        private val uid by lazy { "id --user".exec().trim() }
        private val systemTempDir by lazy { "/tmp/" }
        private val userRuntimeDirInMemory by lazy { "/run/user/$uid" }

        operator fun invoke(args: Array<String>, configure: ParamsForUpdateLocalProperties.() -> Unit) {
            val params = ParamsForUpdateLocalProperties()
            params.configure()

            val devProject = DevProjectLookup.by(args)
            val localPropertiesPlace = devProject.rootStore.place(DevProject.localPropertiesPlace)

            println("LocalPropertiesHelper   rootPlace ${devProject.rootStore.file}")
            println("LocalPropertiesHelper update from ${params.localPathToCurrentFile}")

            val scratches = "/scratches/"
            val consolesIde = "consoles/ide"
            val defaultIdeScript = "ide-scripting.kts"
            val defaultIdeScriptContent = """//
KotlinVersion.CURRENT
"""
            val ideScripting = "$consolesIde/$defaultIdeScript"
            params.params_pathForLookupIdeScriptingPlace.value.trim().let {
                if (it.isNotBlank()) {

                    val ideConfDir = LocalPlace.of(
                        if (it.contains(scratches)) it.split(scratches)[0]
                        else if (it.contains(consolesIde)) it.split(consolesIde)[0]
                        else TODO("unexpected path for lookupIdeScriptingPlace '$it'")
                    )
                    if (ideConfDir.exists()) {
                        val ideScriptingFile = ideConfDir.place(ideScripting)
                        ideScriptingFile create { defaultIdeScriptContent }
                        val consolesIdeDir = ideConfDir.place(consolesIde)
                        if (consolesIdeDir.exists()) {
                            val pathOfIdeScripting = localPropertiesPlace.place(conf_place.of_ide_scripting)
                            println("UpdateHelper.invoke ${consolesIdeDir}")
                            pathOfIdeScripting update consolesIdeDir.path
                        } else TODO()
                    } else TODO()
                }
            }

            if (uid.isEmpty()) TODO()

            val confTmpPlace = fun(it: QuickNamedString.Val, confPlace: String, sysPlace: String, dirName: (String) -> String) {
                val prefix = it.value.trim().ifEmpty { "one" }
                val pathOfTmpConf = localPropertiesPlace.place(confPlace)
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
            val highlightsInfo = normalizeHelper(devProject, highlights)
            println()
//            println("value of 'pathOfTmpDirBig'    is '${devProject.localProperties.pathOfTmpDirBig}'")
//            println("value of 'pathOfTmpDirQuick'  is '${devProject.localProperties.pathOfTmpDirQuick}'")
//            println("value of 'pathOfIdeScripting' is '${devProject.localProperties.pathOfIdeScripting}'")
            println("value of 'pathOfTmpDirBig'    is '${devProject.extPlace.tmpDirBig}'")
            println("value of 'pathOfTmpDirQuick'  is '${devProject.extPlace.tmpDirQuick}'")
            //println("value of 'pathOfIdeScripting' is '${devProject.extPlace.ideScripting}'")


            if (devProject.localProperties.pathOfIdeScripting.isEmpty()) {
                println("path to 'ide scripting' is undefined")
            } else {
                println("value of 'default ide script' is 'file:///${devProject.extPlace.ideScripting.file(defaultIdeScript)}'")
                devProject.extPlace.ideScripting.place(defaultIdeScript) create { defaultIdeScriptContent }
            }

            highlightsInfo.forEach { println(it) }
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

    private fun normalizeHelper(devProject: DevProject, highlights: ParamsOfHighlights = ParamsOfHighlights()): ArrayList<String> {
        val objectName = this::class.java.name
        fun KClass<*>.klassName(): String = java.name.split("$").last()
        val updateHelperName = UpdateHelper::class.klassName()
        val quickNamedStringName = QuickNamedString::class.klassName()
        val quickNamedStringValName = QuickNamedString.Val::class.klassName()
        val quickNamedEmptyName = QuickNamedEmpty::class.klassName()
        val quickNamedEmptyValName = QuickNamedEmpty.Val::class.klassName()
        val quickNamedName = QuickNamed::class.klassName()
        val quickNamedValName = QuickNamed.ValCommon::class.klassName()

        val paramsOfHighlightsName = ParamsOfHighlights::class.java.name
        val paramsForUpdateLocalPropertiesName = ParamsForUpdateLocalProperties::class.java.name

        class ValuePair(val name: String, val value: String) {
            private val columnLength = 16
            private fun String.normalize(): String = let { it + String.format("%" + ((columnLength - it.length).let { if (it < 1) 1 else it }) + "s", " ") }
            fun represent(): String = "${name.normalize()}is 'file:///$value'"
        }

        class ItemValue(val type: QuickNamedType, val name: String, val basePlace: LocalPlace, val valueStr: String = EMPTY_STR_VALUE, val valueList: List<String> = emptyList()) {
            var pref = ""
            fun represent(): String = when (type) {
                QuickNamedType.string, QuickNamedType.highlightInSrc -> "    ${name}..\"${valueStr}\""
                QuickNamedType.highlights -> {
                    "    ${name}(${pref}listOf(\n${
                        valueList.joinToString("") {
                            "        \"${it}\",\n"
                        }
                    }    ))"
                }

                QuickNamedType.highlightsUser -> {
                    "    ${name}(listOf(\n${
                        valueList.joinToString("") {
                            "        \"${it}\",\n"
                        }
                    }    ))"
                }

                else -> TODO()
            }

            fun normalize(): ItemValue = when (type) {
                QuickNamedType.string, QuickNamedType.highlightInSrc -> ItemValue(type, name, basePlace, highlightsFileRelativePath(name, valueStr, basePlace))
                QuickNamedType.highlights,
                QuickNamedType.highlightsUser -> ItemValue(type, name, basePlace, valueList = valueList.map { highlightsFileRelativePath(name, it, basePlace) }).also {
                    if (pref.isNotEmpty()) it.pref = pref
                }

                else -> TODO()
            }

            private fun highlightsFileRelativePath(name: String, value: String, basePlace: LocalPlace): String {
                val notFound = " NOT FOUND"
                var valueFixed = value.trim().trim('\'')
                if (valueFixed.endsWith(notFound)) {
                    valueFixed = valueFixed.substring(0, valueFixed.length - notFound.length)
                }
                val highlightsFilePath = valueFixed.trim()
                if (highlightsFilePath.isNotEmpty()) {
                    val filePathByRelative = basePlace.file(highlightsFilePath)
                    //println("  DEBUG  filePathByRelative   $filePathByRelative")
                    if (filePathByRelative.exists()) {
                        //println("  DEBUG  $name 'file:///${filePathByRelative.absolutePath}'")
                        return highlightsFilePath
                    }
                    val basePath = basePlace.path
                    val highlightsFile = LocalPlace.of(highlightsFilePath)
                    if (highlightsFile.exists()) {
                        val filePath = highlightsFile.path
                        println("$name '${basePath}' 'file:///${filePath}'")
                        val fileRelativePath = if (filePath.startsWith(basePath)) filePath.substring(basePath.length).trim(LocalPlace.separatorChar)
                        else TODO("value of $name for '${basePath}' is invalid . value is :'>$highlightsFilePath<'")
                        return fileRelativePath
                    }
                    return highlightsFilePath + notFound
                }
                return ""
            }

            fun representValues(): List<ValuePair> {
                val list = ArrayList<ValuePair>()
                when (type) {
                    QuickNamedType.string, QuickNamedType.highlightInSrc -> if (valueStr.isNotEmpty()) list.add(ValuePair(name, basePlace.path(valueStr)))
                    QuickNamedType.highlights, QuickNamedType.highlightsUser -> valueList.forEachIndexed { idx, path ->
                        list.add(ValuePair("${name}[$idx]", basePlace.path(path)))
                    }

                    else -> TODO()
                }
                return list
            }
        }

        class Item(val type: QuickNamedType, val name: String, val base: String = EMPTY_STR_VALUE, val valueStr: String = EMPTY_STR_VALUE, val valueList: List<String> = emptyList()) {
            fun value(): ItemValue = when (type) {
                QuickNamedType.string -> ItemValue(type, name, devProject.rootStore, valueStr)
                QuickNamedType.highlightInSrc -> ItemValue(type, name, devProject.src, valueStr)
                QuickNamedType.highlights -> {
                    //println("  DEBUG  $name   $base")
                    var basePlace = devProject.rootStore.file(base)
                    if (!basePlace.exists()) {
                        basePlace = devProject.rootStore.file
                    }
                    ItemValue(type, name, LocalPlace.of(basePlace), valueList = valueList).also { it.pref = if (base.isEmpty() || name == base) "" else "\"${base}\", " }
                }

                QuickNamedType.highlightsUser -> ItemValue(type, name, UserHomePlace, valueList = valueList)

                else -> TODO()
            }.normalize()

            fun represent(): String = value().represent()

            val typeNameRepresent: String = type.klass.klassName()
        }

        fun itemStr(name: String, value: String) = Item(QuickNamedType.string, name, valueStr = value)
        fun itemStrInSrc(name: String, value: String) = Item(QuickNamedType.highlightInSrc, name, valueStr = value)

        //@formatter:on
        val foundFiles = devProject.src.file.walk().onEnter {
            when (it.name) {
                "build", ".idea", ".git" -> {
                    println("skip walk: $it"); false
                }

                else -> true
            }
        }.filter { it.isFile && objectName == /*it.name &&*/ it.nameWithoutExtension }.toList()

        val highlightsList = ArrayList<String>()
        if (foundFiles.size == 1) {
            val thisFile = foundFiles[0]
            val srcPlacePath = devProject.src.path
            val thisFileParentPlace = LocalPlace.of(thisFile).parent.takeIf { it.exists() } ?: TODO()
            val thisFileParentPath = thisFileParentPlace.path
            val thisFileParentRelativePath = if (thisFileParentPath.startsWith(srcPlacePath)) thisFileParentPath.substring(srcPlacePath.length).let {
                if (it.startsWith(LocalPlace.separatorChar)) it.substring(1) else it
            } else TODO()

            val highlightsParams = highlightsParamsOrder.map { (id, type) ->
                with(highlights.propertyByName(id)) {
                    if (this is QuickNamedHighlight.Val) {
                        Item(type, name, base = base, valueList = value)
                    } else if (this is QuickNamedString.Val) {
                        Item(type, name, valueStr = value)
                    } else if (this is QuickNamedEmpty.Val) {
                        Item(type, id)
                    } else TODO()
                }
            }.toTypedArray()

            highlightsList.addAll(highlightsParams.map { it -> it.value() }.flatMap<ItemValue, ValuePair> { it -> it.representValues() }.map { it.represent() })

            val outFileRelativePath = "$thisFileParentRelativePath/$outFileName"
            val paramsNameAndOrder = listOf(
                itemStrInSrc("localPathToCurrentFile", outFileRelativePath),
                *highlightsParams,
                *localPropertiesParamsOrder.map { itemStr(it, "") }.toTypedArray()
            )

            val doNotEditFileIsGenerated = "// do not edit, file is generated"

            val paramsClassStr = """$doNotEditFileIsGenerated
@Suppress("PropertyName")
class $paramsForUpdateLocalPropertiesName {
${paramsNameAndOrder.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.${it.typeNameRepresent}()" }}

    private val map = mapOf(
${paramsNameAndOrder.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedStringName}.$quickNamedStringValName().apply { name = propertyName }
}"""

            val highlightsParamsClassStr = """$doNotEditFileIsGenerated
@Suppress("MemberVisibilityCanBePrivate")
class $paramsOfHighlightsName {
${highlightsParams.sortedBy { it.name }.joinToString("\n") { "    val ${it.name} by ${objectName}.${it.typeNameRepresent}()" }}

    private val map = mapOf(
${highlightsParams.joinToString("\n") { "        \"${it.name}\" to ${it.name}," }}
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: ${objectName}.${quickNamedEmptyName}.$quickNamedEmptyValName()

    fun forEach(action: (${objectName}.${quickNamedName}.$quickNamedValName) -> Unit): Unit = map.values.forEach(action)
}"""

//@Suppress("SpellCheckingInspection")
            val outStr = """$comment
fun main(args: Array<String>) = ${objectName}.${updateHelperName}(args) {
    ${objectName}.highlightsParamsOrder // ... to append 'highlightSrcN'
${paramsNameAndOrder.joinToString("\n") { it.represent() }}
}"""

            thisFileParentPlace.place("${paramsForUpdateLocalPropertiesName}.$fileNameExtension_Kt") update paramsClassStr
            thisFileParentPlace.place("${paramsOfHighlightsName}.$fileNameExtension_Kt") update highlightsParamsClassStr
            thisFileParentPlace.place( outFileName) update outStr

        } else {
            foundFiles.forEach { println("'$objectName' foundFile = $it") }
            TODO("found ${objectName}.$fileNameExtension_Kt files count = ${foundFiles.size}")
        }
        return highlightsList
    }

    @JvmStatic
    fun main(args: Array<String>) {
        normalizeHelper(DevProjectLookup.fromCurrentDir())
    }

    val highlightsParamsOrder = listOf(
        "highlight" to QuickNamedType.string,
        "highlights" to QuickNamedType.highlights,
        "highlights1" to QuickNamedType.highlights,
        "highlights2" to QuickNamedType.highlights,
        "highlights3" to QuickNamedType.highlights,
        "plus1" to QuickNamedType.highlights,
        "plus1named" to QuickNamedType.highlights,
        "src" to QuickNamedType.highlights,
        "highlightSrc" to QuickNamedType.highlightInSrc,
        "highlightSrc1" to QuickNamedType.highlightInSrc,
        "highlightSrc2" to QuickNamedType.highlightInSrc,
        "user" to QuickNamedType.highlightsUser,
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
    private const val outFileName = "eee.$fileNameExtension_Kt"
}