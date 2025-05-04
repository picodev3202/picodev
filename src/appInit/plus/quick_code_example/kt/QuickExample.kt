object QuickExample : MainObject.WithThisFile() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.file}")

    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args) {
        localPathToCurrentSourceFile..""
    }
}