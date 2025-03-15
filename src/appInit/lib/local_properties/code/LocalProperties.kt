class LocalProperties(val store: LocalPlace) : LocalPropertiesConst by LocalPropertiesConst {
    fun readFrom(relativePath: String) = store.place(relativePath).readText().trim()
    val pathOfIdeScripting: String by lazy { readFrom(conf_place.of_ide_scripting) }
    val pathOfTmpDirQuick: String by lazy { readFrom(conf_place.of__tmp_dir_quick).ifEmpty { TODO() } }
    val pathOfTmpDirBig: String by lazy { readFrom(conf_place.of__tmp_dir_big).ifEmpty { TODO() } }
}