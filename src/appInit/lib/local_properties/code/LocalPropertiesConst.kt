interface LocalPropertiesConst {
    @Suppress("ConstPropertyName")
    companion object : LocalPropertiesConst {
        override val conf_place by lazy { Companion }

        const val of__tmp_dir_big = "conf.place.of.tmp_dir_big.txt"
        const val of__tmp_dir_quick = "conf.place.of.tmp_dir_quick.txt"
        const val of_ide_scripting = "conf.place.of.ide-scripting.txt"
    }

    @Suppress("PropertyName")
    val conf_place: Companion
}