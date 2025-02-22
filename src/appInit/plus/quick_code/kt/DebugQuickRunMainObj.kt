object DebugQuickRunMainObj : QuickRun.Main {
    override fun main(thisFile: LocalPlace, args: List<String>) {
        println("$logTagName.main $thisFile")
        println("$logTagName.main $objectName")
    }

    @JvmStatic
    fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(this, args) {
        localPathToCurrentSourceFile..""
    }
}