@Suppress("ClassName")
object firstCmdOfApp {
    @JvmStatic
    fun main(args: Array<String>) {
        println("app.main ${firstCmdOfApp.javaClass.name} $firstCmdOfApp")
        println("app.main ${args.toList()}")
    }
}