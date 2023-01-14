class DevProject(val rootPlace: RootPlace) {
    companion object {
        const val mainDescLocalPath = ".internal/place_config_desc"
        private const val wwgen = "wwgen"
        const val localPropertiesPlace = "$wwgen/local_properties_data"
        const val srcPlaceStr = "src"
        const val genTmpPlace = "$wwgen/tmp"

        fun lookupFromCurrentDir(): DevProject {
            return DevProject(RootPlace.lookupFromCurrentDir(mainDescLocalPath))
        }
    }

    class ExtPlace(
        val ideScripting: LocalFile,
        val tmpDirQuick: LocalFile,
        val tmpDirBig: LocalFile,
    )

    val localProperties by lazy { LocalProperties(rootPlace.file(localPropertiesPlace)) }

    val name: String by lazy {
        val prjDesc = rootPlace.file(mainDescLocalPath).run { if (exists()) readText() else "" }
        val prjName = prjDesc.let { if (it.isNotBlank()) it.lines()[0] else "" }.ifBlank { TODO() }
        prjName
    }

    val srcPlace: LocalFile by lazy { rootPlace.file(srcPlaceStr) }

    fun fileInRootPlace(path: String): LocalFile = rootPlace.file(path)
    fun fileInSrcPlace(path: String): LocalFile = LocalFile(srcPlace, path).absoluteFile
    fun fileInGenTmpPlace(path: String): LocalFile = LocalFile(rootPlace.file(genTmpPlace), path).absoluteFile
    fun fileInTmpDirBig(path: String): LocalFile = LocalFile(extPlace.tmpDirBig, path).absoluteFile
    fun fileInTmpDirQuick(path: String): LocalFile = LocalFile(extPlace.tmpDirQuick, path).absoluteFile

    val extPlace: ExtPlace by lazy {
        ExtPlace(
            LocalFile(localProperties.pathOfIdeScripting),
            LocalFile(localProperties.pathOfTmpDirQuick),
            LocalFile(localProperties.pathOfTmpDirBig),
        )
    }
}