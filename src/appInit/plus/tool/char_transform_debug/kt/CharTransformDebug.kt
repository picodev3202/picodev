object CharTransformDebug : MainObject() {
    override fun main(args: Args) {
//        println("$logTagName.main ${args.array}")
//        println("$logTagName.main ${args.devProject.rootPlace}")
//        println("$logTagName.main $objectName")

        val devProject = args.devProject
        val charsFile = devProject.genTmp.place("${objectName}_chars.txt")
        val stringsFile = devProject.genTmp.place("${objectName}_strings.txt")

        val requiredFiles = listOf(charsFile)
        if (requiredFiles.firstOrNull { !it.exists() }?.exists() == false) {
            requiredFiles.forEach { it update " " }
            println(" data to debug not found please create files: $requiredFiles")
            return
        }

        fun forValidLine(str: String, action: (String) -> Unit) {
            for (line in str.lines()) {
                val lineNormalized = line.trim()
                if (lineNormalized.isNotEmpty()) action(lineNormalized)
            }
        }

        val charsStrings = mutableListOf<String>().apply { forValidLine(charsFile.readText()) { add(it) } }
        val charTransform = CharTransform(charsStrings)

        fun first2other(str: String): String = charTransform.transform(str, 0, 1).value
        fun other2first(str: String): String = charTransform.transform(str, 1, 0).value
        fun debugStringTransform(str: String): Unit = println(" $str : ${other2first(str)} <--> ${first2other(str)}")

        forValidLine(stringsFile.readText()) { debugStringTransform(it) }
        debugStringTransform("git")
        debugStringTransform("one")
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}