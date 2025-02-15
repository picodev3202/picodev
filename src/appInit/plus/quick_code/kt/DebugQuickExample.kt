class DebugQuickExample : QuickRun.Main {
    override fun main(thisFile: LocalPlace, args: List<String>) {
        println("$logTagName.main $thisFile")

        val pref01 = "code."
        val srcFile = thisFile.parent.parent.parent.parent.takeIf { it.exists() }?.file("app_by_script/lua/app.lua") ?: TODO()
        println("srcFile : $srcFile")
        val str = srcFile.readText()
        val list = mutableListOf<String>()
        for (line in str.lines()) {
            val l = line.trim()
            if (l.startsWith(pref01)) {
                val arr = l.split(" = ")
                if (arr.size == 2) {
                    // println(arr[0])
                    list.add(arr[0].substring(pref01.length))
                } else {
                    // println(l)
                }
            }
        }
        // println(list)
        val maxLen = list.maxBy { it.length }
        println("max $maxLen")
        list.sort()
        for (item in list) {
            val out = item + " ".repeat(maxLen.length - item.length) + ""
            // println(out)
            println(""" print("$out ", code.$item) """)
        }
    }
}

fun main(args: Array<String>) = QuickRun.lookupSrcFileByClassNameAndRun(DebugQuickExample(), args) {
    localPathToCurrentSourceFile..""
}