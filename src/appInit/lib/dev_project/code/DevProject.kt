@Suppress("ConstPropertyName", "SpellCheckingInspection")
class DevProject(val rootStore: RootStore) {
    companion object {
        const val mainDescLocalPath = ".internal/place_config_desc"
        private const val wwgen = "wwgen"
        const val localPropertiesPlace = "$wwgen/local_properties_data"
        private const val srcPlace = "src"
        private const val genTmpPlace = "$wwgen/tmp"
    }

    class ExtPlace(val localProperties: LocalProperties) {
        val ideScripting by lazy { LocalPlace.of(localProperties.pathOfIdeScripting) }
        val tmpDirQuick by lazy { LocalPlace.of(localProperties.pathOfTmpDirQuick) }
        val tmpDirBig by lazy { LocalPlace.of(localProperties.pathOfTmpDirBig) }
    }

    val extPlace by lazy { ExtPlace(localProperties) }
    val localProperties by lazy { LocalProperties(rootStore.place(localPropertiesPlace)) }

    val name: String by lazy {
        val prjDesc = rootStore.place(mainDescLocalPath).readText()
        val prjName = prjDesc.let { if (it.isNotBlank()) it.lines()[0] else "" }.ifBlank { TODO() }
        prjName
    }

    val src by lazy { LocalPlace.of(rootStore.file(srcPlace)) }
    val genTmp by lazy { LocalPlace.of(rootStore.file(genTmpPlace)) }
}