object CodeExample : MainObject.WithThisFile() {
    override fun main(args: Args) = main(args.array, args.devProject, args.file)

    private fun main(args: List<String>, devProject: DevProject, thisFile: LocalPlace) {
        println("$logTagName.main ${devProject.rootPlace}")
        println("$logTagName.main $thisFile")

        val absolutePlace = UserHomePlace.place(".m2")
        println("$logTagName.main $absolutePlace")
        println("$logTagName.main ${absolutePlace.file.list()?.toList()}")
        println("$logTagName.main ${absolutePlace.path("repository")}")

    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args) {
        localPathToCurrentSourceFile..""
    }
}