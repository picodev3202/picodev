@Suppress(
    "ClassName",
    "unused",
    "PropertyName",
    "PrivatePropertyName",
    "MoveLambdaOutsideParentheses",
    "RemoveRedundantQualifierName",
)
object appInit : To() {
    class Template {
        // open class SimpleApp(private val lib: Lib = Lib()) : To.Kt({ of(type.KtCode) depends on(lib.one, lib.two) }) {
        //     class Lib : To.General() {
        //         val one = _l - Kt({ of(type.KtCode) })
        //         val two = _l - Kt({ of(type.KtCode) })
        //     }
        // }

        class appInit(val lib: Lib = Lib(), val plus: Plus = Plus(lib)) : To.Kt({ of(type.KtCodeConf) depends on(lib.node_tree, lib.dev_project_lookup) }) {
            class Lib : To.General() {
                val local_place = __________l - Kt({ of(type.KtCode) })
                val local_properties = _____l - Kt({ of(type.KtCode) depends on(local_place) })
                val dev_project = __________l - Kt({ of(type.KtCode) depends on(local_properties) })
                val dev_project_lookup = ___l - Kt({ of(type.KtCode) depends on(dev_project) })
                val node_tree = ____________l - Kt({ of(type.KtCode) depends on(local_place) })
            }

            class Plus(lib: Lib, val tool: Tool = Tool(lib)) : To.General() {
                //private val app0 = SimpleApp()
                //private val app1 = _Kt({ of(type.Kt) }) // unusable by 'app_simple_by_gradle', just for example, only with 'src dir' 'code' by 'of(type.KtCode)' implemented now
                //private val app2 = _Kt({ of(type.KtCode) })
                //private val app3 = SimpleApp()
                private val local_properties_generate_code = _l - __Kt({ of depends on(tool.dev_project_tool_info, tool.local_system) })
                private val quick_code = _____________________l - __Kt({
                    of depends on(tool.main_object, tool.quick_named_string, tool.local_system, tool.dev_project_tool_info, tool.dev_project_tool_run_place)
                })
                private val quick_code_example = _____________l - JvKt({
                    of(type.JvKt) depends on(tool.main_object, tool.quick_named_string, tool.local_system, tool.dev_project_tool_info, tool.dev_project_tool_run_place)
                })
                private val quick_code_plus = ________________l - __Kt({ of depends on(tool.main_object, library.jsoup, library.jackson_databind) })
                private val quick_code_debug = _______________l - __Kt({ of depends on(quick_code) })
                private val scriptTemplate = _________________l - __Kt({ })

                class Tool(lib: Lib) : To.General() {
                    val char_transform = _______________l - Kt({ })
                    val char_transform_debug = _________l - Kt({ of depends on(main_object, char_transform) })
                    val quick_named_string = ___________l - Kt({ })
                    val dev_project_tool_info = ________l - Kt({ of depends on(lib.dev_project_lookup) })
                    val dev_project_tool_run_place = ___l - Kt({ of depends on(lib.dev_project) })
                    val main_object = __________________l - Kt({ of depends on(lib.dev_project_lookup, quick_named_string) })
                    val exec_process = _________________l - Kt({ of depends on(lib.local_place) })
                    val local_user_home = ______________l - Kt({ of depends on(lib.local_place) })
                    val local_system = _________________l - Kt({ of depends on(exec_process, local_user_home) })
                }
            }
        }
    }

    object library {
        val ide_lib = ____________l - Library("ide_lib")
        val jackson_databind = ___l - Library("jackson-databind")
        val jsoup = ______________l - Library("jsoup")
    }

    object srcOf {
        val local_place = __________l - src.appInit.lib.local_place
        val dev_project = __________l - src.appInit.lib.dev_project
        val dev_project_lookup = ___l - src.appInit.lib.dev_project_lookup
        val dev_tool_info = ________l - src.appInit.plus.tool.dev_project_tool_info
        val dev_tool_run_place = ___l - src.appInit.plus.tool.dev_project_tool_run_place
        val quick_named_string = ___l - src.appInit.plus.tool.quick_named_string
        val main_object = __________l - src.appInit.plus.tool.main_object
        val exec_process = _________l - src.appInit.plus.tool.exec_process
        val local_user_home = ______l - src.appInit.plus.tool.local_user_home
        val local_system = _________l - src.appInit.plus.tool.local_system
    }

    abstract class SrcAbstract : To.SrcPlace() {
        val appInit = _______________l - Template.appInit()
    }

    object src : ModuleRegister.src()


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
        println("Init.main KotlinVersion is : ${KotlinVersion.CURRENT}")
    }
}