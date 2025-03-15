import ExecProcess.exec

object LinkAppExample : MainObject() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main $objectName")

        linkApp(args.devProject, "ide")
        linkApp(args.devProject, "ide2")
    }

    fun linkApp(devProject: DevProject, appName: String) {
        val originalAppPath = DevProjectToolInfoLookup.toolValueIn(devProject, appName).takeIf { it.isNotBlank() }
            ?: devProject.rootPlace.place(DevProject.localPropertiesPlace + "/${appName}.txt").readText().trim()

        val binDir = "/bin/"
        val app by QuickNamedStringValue
        val array = originalAppPath.split(binDir)
        val onePlace = LocalPlace.of(array[0].trim())
        val oneName = array[1].trim()
        val oneStore = onePlace.parent.takeIf { it.exists() } ?: TODO()
        val oneAppBinDir = oneStore.place("tool").place(appName).place(binDir).apply { deleteIfExist(); mkdirs() }
        " ln -s $originalAppPath      ${oneAppBinDir.path(app)}    ".exec.waitOutput()
        " ln -s ${originalAppPath}.sh ${oneAppBinDir.path(app)}.sh ".exec.waitOutput()
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}