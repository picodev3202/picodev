@Suppress("ConstPropertyName", "SpellCheckingInspection")
class DevProject(val rootStore: RootPlace) {
    companion object {
        const val mainDescLocalPath = ".internal/place_config_desc"
        private const val wwgen = "wwgen"
        const val localPropertiesPlace = "$wwgen/local_properties_data"
        private const val srcPlace = "src"
        const val genTmpPlace = "$wwgen/tmp"

        fun lookupFromCurrentDir(): DevProject {
            return DevProject(RootPlace.lookupFromCurrentDir(mainDescLocalPath))
        }

        fun lookupBy(args: Array<String>): DevProject {
            return args.firstOrNull()?.let { LocalPlace.of(it) }?.takeIf { it.exists() }
                ?.let { DevProject(RootPlace.lookupToParentOf(it)) }
                ?: lookupFromCurrentDir()
        }
    }

    class ExtPlace(val localProperties: LocalProperties) {
        val ideScripting by lazy { LocalPlace.of(localProperties.pathOfIdeScripting) }
        val tmpDirQuick by lazy { LocalPlace.of(localProperties.pathOfTmpDirQuick) }
        val tmpDirBig by lazy { LocalPlace.of(localProperties.pathOfTmpDirBig) }
    }

    val extPlace by lazy { ExtPlace(localProperties) }
    val localProperties by lazy { LocalProperties(rootStore.place(localPropertiesPlace)) }

    val name: String by lazy {
        val prjDesc = rootStore.file(mainDescLocalPath).run { if (exists()) readText() else "" }
        val prjName = prjDesc.let { if (it.isNotBlank()) it.lines()[0] else "" }.ifBlank { TODO() }
        prjName
    }

    val src by lazy { LocalPlace.of(rootStore.file(srcPlace)) }
    val genTmp by lazy { LocalPlace.of(rootStore.file(genTmpPlace)) }
}