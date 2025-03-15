//
object DebugMainObjectMulti {
    object Main : MainObject() {
        override fun main(args: Args) {
            println("$logTagName.main ${args.array}")
            println("$logTagName.main ${args.devProject.rootPlace}")
            println("$logTagName.main $objectName")
        }

        @JvmStatic
        fun main(args: Array<String>) = MainObject(this, args)
    }

    object MainWithThisFile : MainObject.WithThisFile() {
        override fun main(args: Args) {
            println("$logTagName.main ${args.file}")
            println("$logTagName.main ${args.array}")
            println("$logTagName.main ${args.devProject.rootStore}")
            println("$logTagName.main $objectName")
        }

        @JvmStatic
        fun main(args: Array<String>) = MainObject(this, args)
    }

    object MainWithArgs : MainObject() {
        override fun main(args: Args) {
            println("$logTagName.main ${args.array}")
            println("$logTagName.main ${args.devProject.rootPlace}")
            println("$logTagName.main $objectName")
        }

        @JvmStatic
        fun main(args: Array<String>) {
            debugRunMain(args)
            debugRunMain()
        }

        private fun debugRunMain(args: Array<String>) = MainObject(this, args)
        private fun debugRunMain() = MainObject(this, arrayOf("one", "two"))
    }

    object MainWithArgsFull : MainObject.WithThisFile() {
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

        private fun debugRunMain(args: Array<String>) = MainObject(this, args)
        private fun debugRunMain() = MainObject(this, arrayOf("one", "two"))
    }
}