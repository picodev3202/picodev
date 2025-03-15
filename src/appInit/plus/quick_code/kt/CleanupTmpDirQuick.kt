object CleanupTmpDirQuick : MainObject() {
    override fun main(args: Args) {
        println("$objectName.main " + args.devProject.extPlace.tmpDirQuick.path)
        println("$objectName.main " + args.devProject.extPlace.tmpDirQuick.exists())
        args.devProject.extPlace.tmpDirQuick.deleteIfExist()
        println("$objectName.main " + args.devProject.extPlace.tmpDirQuick.exists())
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}