object DebugToolInfo : MainObject() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main $objectName")

        val info = DevProjectToolInfoLookup.toolInfoIn(args.devProject)
        println("$logTagName.main $info")

        with(ToolInfo) {
            val ideScript = LocalPlace.of(LocalSystem.userConfigDir).place(info.appVendor).place(info.appVersion).place(consolesIde).place(defaultIdeScript)
            println("$logTagName.main ideScript=$ideScript  exists=${ideScript.exists()}")
            println("$logTagName.main content of ideScript='${ideScript.readText()}'")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}