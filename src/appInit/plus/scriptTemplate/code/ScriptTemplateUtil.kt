@Suppress("ClassName")
class ScriptTemplate_QuickNamedValueVal<T : Any> {
    lateinit var value: T
    var name = ""
    operator fun invoke(): T = value
}

@Suppress("ClassName")
class ScriptTemplate_QuickNamedValue<T : Any>(private val defaultValue: T) {
    private val _val = ScriptTemplate_QuickNamedValueVal<T>().apply { value = defaultValue }
    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
}

@Suppress("ClassName")
open class ScriptTemplate_Tool {
    fun file(path: String): java.io.File = java.io.File(path)
}

@Suppress("ClassName")
class ScriptTemplate_BuildScriptNodeApi : ScriptTemplate_Tool() {
    fun apply(from: String) = Unit
}

@Suppress("ClassName")
class ScriptTemplate_BuildScriptNodeApiHelper {
    fun buildscript(block: ScriptTemplate_BuildScriptNodeApi.() -> Unit) {
        ScriptTemplate_BuildScriptNodeApi().block()
    }
}

@Suppress("ClassName")
class ScriptTemplate_OnDoLast {
    val name = ""
    val path = ""
}

@Suppress("ClassName")
class ScriptTemplate_TackRegister {
    fun doLast(block: ScriptTemplate_OnDoLast.() -> Unit) {}
}

@Suppress("ClassName")
class ScriptTemplate_Tasks {
    fun register(taskName: String, block: ScriptTemplate_TackRegister.() -> Unit) {}
}

@Suppress("ClassName")
class ScriptTemplate_TasksNodeApiHelper {
    val tasks = ScriptTemplate_Tasks()
}

@Suppress("ClassName")
class ScriptTemplate_Holder<T : Any>(value: T) {
    val uniqueNameHolder by ScriptTemplate_QuickNamedValue(value)
}

@Suppress("ClassName")
class ScriptTemplate_EmulateFor {
    val buildScriptNodeApi = ScriptTemplate_Holder(ScriptTemplate_BuildScriptNodeApiHelper())
    val tasksNodeApi = ScriptTemplate_Holder(ScriptTemplate_TasksNodeApiHelper())
}

class ScriptTemplateUtil {
    val emulateFor by ScriptTemplate_QuickNamedValue(ScriptTemplate_EmulateFor())
    val srcRootsOfSimpleAppTemplate by ScriptTemplate_QuickNamedValue("")
    private fun simpleName(@Suppress("LocalVariableName") java_class: Class<*>): String = java_class.name.split("$").last()

    fun scriptTextFromTemplate(thisFileAbsolutePath: String, templateRelativePath: String, srcRootsOfSimpleApp: List<String>): String {
        val thisFile = ScriptTemplate_Tool().file(thisFileAbsolutePath)
        val scriptTemplateFile = java.io.File(thisFile.parentFile, templateRelativePath)
            .run { if (exists() && isFile) absoluteFile else if (exists() && isDirectory) listFiles()?.first() else null } ?: TODO()
        val scriptTemplateUtilObj = ScriptTemplateUtil()
        val scriptTemplateUtilClassName = simpleName(ScriptTemplateUtil().javaClass)
        val marker1 = ".${ScriptTemplate_Holder(0).uniqueNameHolder.name}()."
        val marker2 = "${scriptTemplateUtilClassName}().${scriptTemplateUtilObj.emulateFor.name}()."
        val marker3 = "${scriptTemplateUtilClassName}().${scriptTemplateUtilObj.srcRootsOfSimpleAppTemplate.name}()"
        val parts = scriptTemplateFile.readText().split(marker1)
        var out = ""
        if (parts.size >= 2) for (i in 1 until parts.size)
            for (line in parts[i].lines())
                if (!line.contains(marker2))
                    out += if (line.contains(marker3)) "${line.replace(marker3, srcRootsOfSimpleApp.joinToString { "\"$it\"" })} \n" else "$line\n"
        return out
    }
}