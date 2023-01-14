@Suppress("ClassName")
object firstCmdOfApp {

    // unusable by 'app_simple_by_gradle', just for example, only with 'src dir' 'code' implemented now
    @JvmStatic
    fun main(args: Array<String>) {
        println("app.main ${firstCmdOfApp.javaClass.name} $firstCmdOfApp")
        println("app.main ${args.toList()}")
    }
}
//     unusable by 'app_simple_by_gradle', just for example, only with 'src dir' 'code' implemented now
