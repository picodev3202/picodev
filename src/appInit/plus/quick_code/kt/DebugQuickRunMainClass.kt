class DebugQuickRunMainClass : QuickRun.Main {
    override fun main(thisFile: LocalFile, args: List<String>) {
        println("DebugClass.main $thisFile")
    }
}

fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(DebugQuickRunMainClass(), args) {
    localPathToCurrentSourceFile..""
}