object DebugQuickRunMainObj : QuickRun.Main {
    override fun main(thisFile: LocalFile, args: List<String>) {
        println("DebugObj.main $thisFile")
    }

    @JvmStatic
    fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(this, args) {
        localPathToCurrentSourceFile..""
    }
}