import kotlin.reflect.KClass

@Suppress(
    "ConstPropertyName",
    "LocalVariableName",
)
object LocalPropertiesQuickHelperTool : LocalPropertiesConst by LocalPropertiesConst {

    const val EMPTY_STR_VALUE = ""

    sealed class QuickNamed<T : QuickNamed.ValCommon>(private val _val: T) {
        abstract class ValCommon {
            var name = EMPTY_STR_VALUE

            //override fun toString() = "(name=$name, value=$value)"
            abstract fun update(content: ValCommon)
        }

        operator fun getValue(@Suppress("unused") thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
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
        companion object {
            val string = QuickNamedType(QuickNamedString::class)
            val highlightInSrc = QuickNamedType(QuickNamedString::class)
            val highlights = QuickNamedType(QuickNamedHighlight::class)
            val highlightsUser = QuickNamedType(QuickNamedHighlight::class)
        }
    }

    object UpdateHelper {
        private val uid by lazy { LocalSystem.userId }
        private val systemTempDir by lazy { LocalSystem.tempDir }
        private val userRuntimeDirInMemory by lazy { LocalSystem.userRuntimeDir }

        operator fun Array<String>.component1() = takeIf { it.size >= 1 }?.let { it[0] } ?: ""
        operator fun Array<String>.component2() = takeIf { it.size >= 2 }?.let { it[1] } ?: ""
        operator fun Array<String>.component3() = takeIf { it.size >= 3 }?.let { it[2] } ?: ""
        operator fun Array<String>.component4() = takeIf { it.size >= 4 }?.let { it[3] } ?: ""
        operator fun Array<String>.component5() = takeIf { it.size >= 5 }?.let { it[4] } ?: ""
        operator fun Array<String>.component6() = takeIf { it.size >= 6 }?.let { it[5] } ?: ""
        operator fun Array<String>.component7() = takeIf { it.size >= 7 }?.let { it[6] } ?: ""
        operator fun Array<String>.component8() = takeIf { it.size >= 8 }?.let { it[7] } ?: ""
        operator fun Array<String>.component9() = takeIf { it.size >= 9 }?.let { it[8] } ?: ""
        operator fun Array<String>.component10() = takeIf { it.size >= 10 }?.let { it[9] } ?: ""
        operator fun Array<String>.component11() = takeIf { it.size >= 11 }?.let { it[10] } ?: ""
        operator fun Array<String>.component12() = takeIf { it.size >= 12 }?.let { it[11] } ?: ""

        operator fun invoke(args: Array<String>, configure: ParamsForUpdateValues.() -> Unit) = with(ToolInfo) {
            //try {
            //    println(args.toList())
            //    val (ktFile, projectFileDir, moduleDir, fileDir, filePath, clipboardContent, tmp1, tmp2, tmp3) = args
            //    println("ktFile           '$ktFile'")
            //    println("projectFileDir   '$projectFileDir'")
            //    println("moduleDir        '$moduleDir'")
            //    println("fileDir          '$fileDir'")
            //    println("filePath         '$filePath'")
            //    println("clipboardContent '$clipboardContent'")
            //    println("tmp1 '$tmp1'")
            //    println("tmp2 '$tmp2'")
            //    println("tmp3 '$tmp3'")
            //} catch (e: Exception) {
            //    e.printStackTrace()
            //}

            val params = ParamsForUpdateValues()
            params.configure()

            val devProject = DevProjectLookup.by(args)
            val localPropertiesPlace = devProject.rootPlace.place(DevProject.localPropertiesPlace)

            println("LocalProperties update from file://${devProject.src.path(params.localPathToCurrentFile.value)}")

            params.params_pathForLookupIdeScriptingPlace.value.trim().let {
                val ideConfDir = if (it.isBlank()) {
                    val info = DevProjectToolInfoLookup.toolInfoIn(devProject)
                    LocalPlace.of(LocalSystem.userConfigDir).place(info.appVendor).place(info.appVersion).takeIf { info.appVendor.isNotEmpty() && info.appVersion.isNotEmpty() }
                } else {
                    LocalPlace.of(
                        if (it.contains(scratches)) it.split(scratches)[0]
                        else if (it.contains(consolesIde)) it.split(consolesIde)[0]
                        else TODO("unexpected path for lookupIdeScriptingPlace '$it'")
                    )
                }
                if (ideConfDir?.exists() == true) {
                    val ideScriptingFile = ideConfDir.place(ideScripting)
                    ideScriptingFile createSilent  { defaultIdeScriptContent }
                    val consolesIdeDir = ideConfDir.place(consolesIde)
                    if (consolesIdeDir.exists()) {
                        val pathOfIdeScripting = localPropertiesPlace.place(conf_place.of_ide_scripting)
                        // println("UpdateHelper.invoke $consolesIdeDir")
                        pathOfIdeScripting updateSilent consolesIdeDir.path
                    } else TODO()
                }
            }

            if (uid.isEmpty()) TODO()

            val confTmpPlace = fun(it: QuickNamedString.Val, confPlace: String, sysPlace: String, useStrict: Boolean, dirName: (String) -> String) {
                val prefix = it.value.trim().ifEmpty { "one" }
                val pathOfTmpConf = localPropertiesPlace.place(confPlace)
                if (!LocalPlace.of(sysPlace).exists()) TODO("'$sysPlace' not exists")
                val tmpDirNewPath = "$sysPlace/${dirName(prefix)}"
                val tmpDirNew = LocalPlace.of(tmpDirNewPath).file
                if (useStrict && tmpDirNew.exists()) TODO("'$tmpDirNew' already exists, please use another prefix for '${it.name}'")
                pathOfTmpConf updateSilent tmpDirNew.absolutePath
                // if (!useStrict) println("UpdateHelper confTmpPlace ${tmpDirNew.absolutePath}")
            }

            val highlights = ParamsForUpdateValues()
            highlights.forEach { it.update(params.propertyByName(it.name)) }

            val devPlaceRootName = LocalPlace.of(with(ExecProcess) { "realpath ${devProject.rootPlace.path}".exec.waitOutput().toString() }).name
            val devPlaceName = devProject.name
            val devPlaceUid = "_u_${devPlaceRootName}__$devPlaceName"
            val updatedName: (String) -> String = { "${devPlaceUid}/_${it}_" }

            confTmpPlace(params.params_prefixOfTmpDirBig, conf_place.of__tmp_dir_big, systemTempDir, false) { "${devPlaceUid}_${uid}__$it" }
            confTmpPlace(params.params_prefixOfTmpDirQuick, conf_place.of__tmp_dir_quick, userRuntimeDirInMemory, false, updatedName)

            // println()
            val highlightsInfo = normalizeHelper(devProject, highlights)
            // println()
            println("value of 'pathOfTmpDirBig'    is '${devProject.extPlace.tmpDirBig}'")
            println("value of 'pathOfTmpDirQuick'  is '${devProject.extPlace.tmpDirQuick}'")

            if (devProject.localProperties.pathOfIdeScripting.isEmpty()) {
                println("path to 'ide scripting' is undefined")
            } else {
                println("value of 'default ide script' is 'file://${devProject.extPlace.ideScripting.file(defaultIdeScript)}'")
                devProject.extPlace.ideScripting.place(defaultIdeScript) createSilent { defaultIdeScriptContent }
            }

            highlightsInfo.forEach { println(it) }

            "highlights.txt".let { highlights_txt ->
                if (devProject.localProperties.store.place(highlights_txt).exists()) {
                    devProject.localProperties.readFrom(highlights_txt).lines().forEach {
                        it.takeIf { it.isNotBlank() }?.let {
                            println("from highlights.txt: file://$it")
                        }
                    }
                } else {
                    devProject.localProperties.store.place(highlights_txt).let { it update it.path }
                }
            }

            confTmpPlace(params.params_prefixOfTmpDirBig, conf_place.of__tmp_dir_big, systemTempDir, true) { "${devPlaceUid}__${uid}_$it" }
            confTmpPlace(params.params_prefixOfTmpDirQuick, conf_place.of__tmp_dir_quick, userRuntimeDirInMemory, true, updatedName)
        }
    }

    private fun normalizeHelper(devProject: DevProject, highlights: ParamsForUpdateValues = ParamsForUpdateValues()): ArrayList<String> {
        val objectName = this::class.java.name
        fun KClass<*>.klassName(): String = java.name.split("$").last()
        val updateHelperName = UpdateHelper::class.klassName()
        val quickNamedStringName = QuickNamedString::class.klassName()
        val quickNamedStringValName = QuickNamedString.Val::class.klassName()
        val quickNamedName = QuickNamed::class.klassName()
        val quickNamedValName = QuickNamed.ValCommon::class.klassName()

        val paramsForUpdateLocalPropertiesName = ParamsForUpdateValues::class.java.name

        class ValuePair(val name: String, val value: String) {
            private val columnLength = 17
            private fun String.normalize(): String = let { it + String.format("%" + ((columnLength - it.length).let { if (it < 1) 1 else it }) + "s", " ") }
            fun represent(): String = "${name.normalize()}is 'file://${LocalPlace.of(value).path}'"
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
                QuickNamedType.string -> ItemValue(type, name, devProject.rootPlace, valueStr)
                QuickNamedType.highlightInSrc -> ItemValue(type, name, devProject.src, valueStr)
                QuickNamedType.highlights -> {
                    //println("  DEBUG  $name   $base")
                    var basePlace = devProject.rootPlace.file(base)
                    if (!basePlace.exists()) {
                        basePlace = devProject.rootPlace.file
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

    fun forEach(action: (${objectName}.${quickNamedName}.$quickNamedValName) -> Unit): Unit = map.values.forEach(action)
}"""

//@Suppress("SpellCheckingInspection")
            val outStr = """$comment
fun main(args: Array<String>) = ${objectName}.${updateHelperName}(args) {
    ${objectName}.highlightsParamsOrder // ... to append 'highlight...'
${paramsNameAndOrder.joinToString("\n") { it.represent() }}
}"""

            thisFileParentPlace.place("${paramsForUpdateLocalPropertiesName}.$fileNameExtension_Kt") updateSilent paramsClassStr
            thisFileParentPlace.place(outFileName) updateSilent outStr

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