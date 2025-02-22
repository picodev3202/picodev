object DebugQuickRunMainObj : QuickRun.By.Main() {
    override fun main(thisFile: ThisFile, args: List<String>) {
        println("$logTagName.main $thisFile")
        println("$logTagName.main $objectName")
    }

    @JvmStatic
    fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(this, args) {
        localPathToCurrentSourceFile..""
    }
}