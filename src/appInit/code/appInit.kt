@Suppress("ClassName", "unused", "PropertyName", "MoveLambdaOutsideParentheses")
object appInit : NodeItems() {
    class Template {
        open class suit_board_code : _General() {
            val board_code = _Py({ of(type.BoardCode) })
            val code = _____KtJv({ of(type.JvKt) depends on(src.appInit.lib.dev_project, library.jSerialComm_2_9_2) })
        }

        open class SimpleApp(private val lib: Lib = Lib()) : _Kt({ of(type.KtCode) depends on(lib.one, lib.two) }) {
            class Lib : _General() {
                val one = _Kt({ of(type.KtCode) })
                val two = _Kt({ of(type.KtCode) })
            }
        }

        @Suppress("unused", "PropertyName")
        open class appInit(val lib: Lib = Lib(), val plus: Plus = Plus(lib)) : _Kt({ of(type.KtCode) depends on(lib.node_tree, lib.dev_project) }) {
            open class Lib : _General() {
                val node_tree = ___Kt({ of(type.KtCode) })
                val dev_project = _Kt({ of(type.KtCode) })
            }

            open class Plus(libOfAppInit: Lib) : _General() {
                //private val app0 = SimpleApp()
                //private val app1 = _Kt({ of(type.Kt) }) // unusable by 'app_simple_by_gradle', just for example, only with 'src dir' 'code' by 'of(type.KtCode)' implemented now
                //private val app2 = _Kt({ of(type.KtCode) })
                //private val app3 = SimpleApp()
                val local_properties_generate_code = _Kt({ of(type.Kt) depends on(libOfAppInit.dev_project) })
                val quick_run_main = _________________Kt({ of(type.Kt) depends on(libOfAppInit.dev_project) })
                private val quick_code = _____________Kt({ of(type.Kt) depends on(quick_run_main) })
                private val scriptTemplate = _________Kt({ })
            }
        }
    }

    object library {
        val jSerialComm_2_9_2 = _Library("jSerialComm-2.9.2") //https://github.com/Fazecast/jSerialComm/releases/tag/v2.11.0
    }

    object src : SrcPlace() {
        val appInit = Template.appInit()

        object board_code : Template.suit_board_code()
    }

    val LocalFile.deleteIfExist: Boolean
        get() {
            if (exists()) {
                if (isDirectory) deleteRecursively()
                else delete()
                println("remove: $absolutePath")
                return true
            }
            return false
        }

    @JvmStatic
    fun main(args: Array<String>) {
        val utilModulesXml = IdeModulesXml
        val util = UtilSelectModuleItems

        val devProject = DevProject.lookupBy(args)
        val place = devProject.rootStore.file
        val placeSrc = devProject.src
        val prjName = devProject.name

        println("Init.main place=$place projectName=$prjName")

        val modulesRelativePath = mutableListOf<String>()
        val holder = util.select(src)
        holder.forEach { item ->
            // /* if (node.childrenIdx.isEmpty()) */ LocalFile(placeSrc, path.fs).mkdirs()
            val moduleItemType = item.module.type
            println("Init.main ${item.path} ${moduleItemType is DefaultTypes.ItemType}")
            if (moduleItemType is DefaultTypes.ItemType) {
                modulesRelativePath.add(utilModulesXml.moduleGeneric(prjName, place, placeSrc.name, item.path, moduleItemType, item.allDependency, item.libraries))
                val moduleDir = placeSrc.file(item.path.fs).apply { mkdirs() }
                item.copyContentOf?.run {
                    val from = placeSrc.file(fs)
                    println("Init.main  copy from : $from to : $moduleDir")
                    from.walk().onEnter {file -> when (file.name) {//@formatter:off
                        ".gradle", "build" -> {                  file.deleteIfExist; false}
                        ".idea", ".git" -> { println("skip walk: $file"); false}
                        else -> true                 }
                    }.forEach {}
                    from.copyRecursively(moduleDir, overwrite = false, onError = { _, exception ->
                        println("Init.main copy exception $exception")
                        OnErrorAction.SKIP
                    })
                }
            }
        }
        println("Init.main modules count ${modulesRelativePath.size}")
        if (modulesRelativePath.isNotEmpty()) {
            val modulesXmlStr = utilModulesXml.modulesXml(modulesRelativePath)
            val modulesFile = LocalFile(place, "$prjName/.idea/modules.xml")
            println("Init.main $modulesFile")
            modulesFile.apply { parentFile.mkdirs() }.writeText(modulesXmlStr)
        }
    }
}