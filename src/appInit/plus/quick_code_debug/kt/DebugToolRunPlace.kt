object DebugToolRunPlace : MainObject() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main $objectName")

        val runPlace = ToolRunPlace.from(args.devProject)
        println("$logTagName.main $runPlace")
        println("$logTagName.main ${runPlace.isValid}")
        println("$logTagName.main ${args.devProject.rootPlace.path(DevProject.wwgen)}")
        println("$logTagName.main ${runPlace.place("file_from_app_by_script").readText().trim()}")
        val value = args.devProject.rootPlace.path(DevProject.wwgen)
        val valueFromFile = runPlace.place("file_from_app_by_script").readText().trim()
        println("$logTagName.main (value == valueFromFile)=${value == valueFromFile}")
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}