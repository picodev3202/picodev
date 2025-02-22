//
object DebugMainObjectMulti {
    object Main : MainObject() {
        override fun main(args: Args) {
            println("$logTagName.main ${args.file}")
            println("$logTagName.main ${args.array}")
            println("$logTagName.main ${args.devProject.rootStore}")
            println("$logTagName.main $objectName")
        }

        @JvmStatic
        fun main(args: Array<String>) = MainObject(this, args) {
            localPathToCurrentSourceFile..""
        }
    }

    object MainWithArgs : MainObject() {
        override fun main(args: Args) {
            println("$logTagName.main ${args.file}")
            println("$logTagName.main ${args.array}")
            println("$logTagName.main ${args.devProject.rootStore}")
            println("$logTagName.main $objectName")
        }

        @JvmStatic
        fun main(args: Array<String>) {
            debugRunMain(args)
            debugRunMain()
        }

        private fun debugRunMain(args: Array<String>) = MainObject(this, args) {
            localPathToCurrentSourceFile..""
        }

        private fun debugRunMain() = MainObject(this, arrayOf("one", "two")) {
            localPathToCurrentSourceFile..""
        }
    }
}