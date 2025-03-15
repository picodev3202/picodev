//
class DebugMainObjectByClass : MainObject.WithThisFile() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.file}")
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootStore}")
        println("$logTagName.main $objectName")
    }
}

fun main(args: Array<String>) = MainObject(DebugMainObjectByClass(), args)