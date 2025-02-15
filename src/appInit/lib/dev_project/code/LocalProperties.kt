class LocalProperties(private val localPropertiesPlace: LocalPlace) : LocalPropertiesConst by LocalPropertiesConst {
    private fun readFrom(relativePath: String) = localPropertiesPlace.file(relativePath).run { if (exists()) readText().trim() else "" }
    val pathOfIdeScripting: String by lazy { readFrom(conf_place.of_ide_scripting) }
    val pathOfTmpDirQuick: String by lazy { readFrom(conf_place.of__tmp_dir_quick).ifEmpty { TODO() } }
    val pathOfTmpDirBig: String by lazy { readFrom(conf_place.of__tmp_dir_big).ifEmpty { TODO() } }
}