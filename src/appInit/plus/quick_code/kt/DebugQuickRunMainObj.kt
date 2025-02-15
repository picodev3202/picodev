object DebugQuickRunMainObj : QuickRun.Main {
    override fun main(thisFile: LocalPlace, args: List<String>) {
        println("DebugObj.main $thisFile")
    }

    @JvmStatic
    fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(this, args) {
        localPathToCurrentSourceFile..""
    }
}