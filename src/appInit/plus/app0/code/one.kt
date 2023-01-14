@Suppress("ClassName")
object one {
    @JvmStatic
    fun main(args: Array<String>) {
        println("one.main  ${AppLibOne.javaClass.name} $AppLibTwo")
        println("one.main  ${args.toList()}")
    }
}