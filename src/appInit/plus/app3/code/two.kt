@Suppress("ClassName")
object two {
    @JvmStatic
    fun main(args: Array<String>) {
        println("two.main ${App2LibOne.javaClass.name} $App2LibTwo")
        println("two.main  ${args.toList()}")
    }
}