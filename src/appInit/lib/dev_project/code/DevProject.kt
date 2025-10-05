@Suppress(
    "ConstPropertyName",
    "SpellCheckingInspection",
)
class DevProject(val rootPlace: LocalPlace) {
    companion object {
        const val mainDescLocalPath = ".internal/place_config_desc"
        const val wwgen = "wwgen"
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
    val localProperties by lazy { LocalProperties(rootPlace.place(localPropertiesPlace)) }

    val name: String by lazy {
        val prjDesc = rootPlace.place(mainDescLocalPath).readText()
        val prjName = prjDesc.let { if (it.isNotBlank()) it.lines()[0] else "" }.ifBlank { TODO() }
        prjName
    }

    val src by lazy { LocalPlace.of(rootPlace.file(srcPlace)) }
    val genTmp by lazy { LocalPlace.of(rootPlace.file(genTmpPlace)) }

    fun place(relativePath: String) = rootPlace.place(relativePath)

    override fun toString() = "$name (${Integer.toHexString(hashCode())}) [file://$rootPlace]"

}