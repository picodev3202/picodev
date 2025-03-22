@Suppress(
    "ClassName",
    "unused",
    "PropertyName",
    "PrivatePropertyName",
    "MoveLambdaOutsideParentheses",
)
object appInit : NodeItems() {
    class Template {
        open class SimpleApp(private val lib: Lib = Lib()) : Kt({ of(type.KtCode) depends on(lib.one, lib.two) }) {
            class Lib : _General() {
                val one = ___Kt({ of(type.KtCode) })
                val two = ___Kt({ of(type.KtCode) })
            }
        }

        open class SampleAppMulti(private val lib: Lib = Lib()) : JvKt({ of depends on(lib.oneKt, lib.oneJvKt) }) {
            class Lib : _General() {
                val oneJv = ____l - __Jv({ })
                val twoJv = ____l - __Jv({ of depends on(oneJv) })
                val fourJv = ___l - __Jv___of_depends_on(oneJv)
                val oneJvKt = __l - JvKt({ })
                val twoKtJv = __l - KtJv({ of depends on(oneJvKt, oneKt) })
                val oneKt = ____l - __Kt({ of depends on(oneJvKt) })
                val twoKt = ____l - __Kt({ of depends on(oneJvKt, oneKt) })
                val fourKt = ___l - __Kt___of_depends_on(oneJvKt, oneKt)
                val fiveJvKt = _l - JvKt___of_depends_on(oneJvKt, oneKt)
                val fiveKtJv = _l - KtJv___of_depends_on(oneJvKt, oneKt)
                val onePy = ____l - __Py({ })
                val twoPy = ____l - __Py({ of depends on(onePy) })
                val fourPy = ___l - __Py___of_depends_on(onePy)
            }
        }

        open class appInit(val lib: Lib = Lib(), val plus: Plus = Plus(lib)) : Kt({ of(type.KtCode) depends on(lib.node_tree, lib.dev_project_lookup) }) {
            class Lib : ____________General() {
                val local_place = _____________Kt({ of(type.KtCode) })
                val local_properties = ________Kt({ of(type.KtCode) depends on(local_place) })
                val dev_project = _____________Kt({ of(type.KtCode) depends on(local_properties) })
                val dev_project_lookup = ______Kt({ of(type.KtCode) depends on(dev_project) })
                val node_tree = _______________Kt({ of(type.KtCode) depends on(local_place) })
            }

            class Plus(lib: Lib, val tool: Tool = Tool(lib)) : _General() {
                //private val app0 = SimpleApp()
                //private val app1 = _Kt({ of(type.Kt) }) // unusable by 'app_simple_by_gradle', just for example, only with 'src dir' 'code' by 'of(type.KtCode)' implemented now
                //private val app2 = _Kt({ of(type.KtCode) })
                //private val app3 = SimpleApp()
                private val local_properties_generate_code = _l - __Kt({ of depends on(tool.dev_project_tool_info, tool.local_system) })
                private val quick_code = _____________________l - KtJv({
                    of(type.JvKt) depends on(tool.main_object, tool.quick_named_string, tool.local_system, tool.dev_project_tool_info, tool.dev_project_tool_run_place)
                })
                private val quick_code_debug = _________________l - Kt({ of depends on(quick_code) })
                private val scriptTemplate = ___________________l - Kt({ })

                class Tool(lib: Lib) : _General() {
                    val quick_named_string = ________l - Kt({ })
                    val dev_project_tool_info = _____l - Kt({ of depends on(lib.dev_project_lookup) })
                    val dev_project_tool_run_place = l - Kt({ of depends on(lib.dev_project) })
                    val main_object = _______________l - Kt({ of depends on(lib.dev_project_lookup, quick_named_string) })
                    val exec_process = ______________l - Kt({ of depends on(lib.local_place) })
                    val local_user_home = ___________l - Kt({ of depends on(lib.local_place) })
                    val local_system = ______________l - Kt({ of depends on(exec_process, local_user_home) })
                }
            }
        }
    }

    object library {
        val ide_lib = ___Library("ide_lib")
    }

    object srcOf {
        val local_place = ________l - src.appInit.lib.local_place
        val dev_project = ________l - src.appInit.lib.dev_project
        val dev_project_lookup = _l - src.appInit.lib.dev_project_lookup
        val dev_tool_info = ______l - src.appInit.plus.tool.dev_project_tool_info
        val dev_tool_run_place = _l - src.appInit.plus.tool.dev_project_tool_run_place
        val quick_named_string = _l - src.appInit.plus.tool.quick_named_string
        val main_object = ________l - src.appInit.plus.tool.main_object
        val exec_process = _______l - src.appInit.plus.tool.exec_process
        val local_user_home = ____l - src.appInit.plus.tool.local_user_home
    }

    object src : SrcPlace() {
        val appInit = _____l - Template.appInit()
        val appMulti = ____l - Template.SampleAppMulti()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val utilModulesXml = IdeModulesXml
        val util = UtilSelectModuleItems

        val devProject = DevProjectLookup.by(args)
        val place = devProject.rootPlace
        val placeSrc = devProject.src
        val prjName = devProject.name

        println("Init.main place=$place projectName=$prjName")

        val modulesRelativePath = mutableListOf<String>()
        val holder = util.select(src)
        holder.forEachSmart { item ->
            // /* if (node.childrenIdx.isEmpty()) */ LocalFile(placeSrc, path.fs).mkdirs()
            val moduleItemType = item.module.type
            // println("Init.main  ${item.path} ${moduleItemType is DefaultTypes.ItemType}")
            if (moduleItemType is DefaultTypes.ItemType) {
                modulesRelativePath.add(utilModulesXml.moduleGeneric(prjName, place, placeSrc.name, item.path, moduleItemType, item.allDependency, item.libraries))
                val moduleDir = placeSrc.file(item.path.fs).apply { mkdirs() }
                item.copyContentOf?.run {
                    val from = placeSrc.file(fs)
                    println("Init.main  copy from : $from to : $moduleDir")
                    from.walk().onEnter {file -> when (file.name) {//@formatter:off
                        ".gradle", "build" -> { LocalPlace.of(file).deleteIfExist(); false}
                        ".idea", ".git" -> { println("skip walk: $file"); false}
                        else -> true                 }
                    }.forEach {}
                    from.copyRecursively(moduleDir, overwrite = false, onError = { _, exception ->
                        println("Init.main copy exception $exception")
                        OnErrorAction.SKIP
                    })
                }

            }
        } //@formatter:on

        println("Init.main")
        holder.forEachSmart({ item -> item.module.type is DefaultTypes.ItemType }) { item ->
            val modulePlace = placeSrc.place(item.path.fs).apply { mkdirs() }
            item.renameTo?.trim()?.takeIf { it.isNotEmpty() && modulePlace.name != it }?.let {
                val moduleDirNew = modulePlace.parent.file(it)
                println("Init.main $modulePlace renameTo $moduleDirNew")
                modulePlace.file.renameTo(moduleDirNew)
            }
        }

        println("Init.main modules count ${modulesRelativePath.size}")
        if (modulesRelativePath.isNotEmpty()) {
            val modulesXmlStr = utilModulesXml.modulesXml(modulesRelativePath)
            val modulesFile = place.file("$prjName/.idea/modules.xml")
            println("Init.main $modulesFile")
            modulesFile.apply { parentFile.mkdirs() }.writeText(modulesXmlStr)
        }
    }
}