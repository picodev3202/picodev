//
object DebugMainObject : MainObject() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main $objectName")
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args) {
        localPathToCurrentSourceFile..""
    }
}