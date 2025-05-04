object CharTransformDebugSimple {
    fun CharTransform.first2other(str: String): CharTransform.Result = transform(str, 0, 1)
    fun CharTransform.other2first(str: String): CharTransform.Result = transform(str, 1, 0)
    fun CharTransform.debugStringTransform(str: String): Unit = println("$str : ${other2first(str)} <--> ${first2other(str)} | $str : ${other2first(str).value} <--> ${first2other(str).value}")

    @JvmStatic
    fun main(args: Array<String>) {
        debug01()
        println("----")
    }

    fun debug01() {
        val charTransform = listOf(
            """ git """,
            """ tig """,
        ).let { CharTransform(it) }
        charTransform.debugStringTransform("git")
        charTransform.debugStringTransform("tig")
    }
}