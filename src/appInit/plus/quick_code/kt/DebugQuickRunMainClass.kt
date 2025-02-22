class DebugQuickRunMainClass : QuickRun.Main {
    override fun main(thisFile: LocalPlace, args: List<String>) {
        println("$logTagName.main $thisFile")
        println("$logTagName.main $objectName")
    }
}

fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(DebugQuickRunMainClass(), args) {
    localPathToCurrentSourceFile..""
}