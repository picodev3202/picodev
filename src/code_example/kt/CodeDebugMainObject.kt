//
object CodeDebugMainObject : MainObject() {
    override fun main(args: Args) = main(args.array, args.devProject)

    private fun main(args: List<String>, devProject: DevProject) {
        println("$logTagName.main $args")
        println("$logTagName.main ${devProject.rootPlace}")
        println("$logTagName.main $objectName")
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args) {
        localPathToCurrentSourceFile..""
    }
}