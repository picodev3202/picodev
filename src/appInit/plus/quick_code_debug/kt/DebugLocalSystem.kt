object DebugLocalSystem : MainObject() {
    override fun main(args: Args) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main $objectName")

        println("$logTagName.main ${LocalSystem.tempDir}")
        println("$logTagName.main ${LocalSystem.userId}")
        println("$logTagName.main ${LocalSystem.userRuntimeDir}")

        println("$logTagName.main")

        System.getProperties().forEach {
            val (key, value) = it
            println("$logTagName.main property: '$key' '$value'")
        }
        println("$logTagName.main")
        System.getenv().forEach {
            val (key, value) = it
            println("$logTagName.main      env: '$key' '$value'")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}