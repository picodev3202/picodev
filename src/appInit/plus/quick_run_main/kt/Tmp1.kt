object Tmp1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val devProject = DevProject.lookupBy(args)

        val resStr = "ls -l".exec()
        println("Tmp1.main $resStr")

        val resStr2 = "ls -l".exec {
            directory(devProject.src.file)
        }
        println("Tmp1.main $resStr2")

        devProject.genTmp.file.let {
            if (it.exists()) {
                val resStr3 = "ls -l".exec {
                    directory(it)
                }
                println("Tmp1.main $resStr3")
            }
        }

        devProject.genTmp.place("tmp1.txt") update " "
    }

    fun String.exec() = exec { }
    fun String.exec(block: ProcessBuilder.() -> Unit): String {
        val processBuilder = ProcessBuilder("sh", "-c", this)
        processBuilder.block()
        val process = processBuilder.start()
        var out = ""
        java.io.BufferedReader(java.io.InputStreamReader(process.inputStream)).use { input ->
            var line: String?
            while (input.readLine().also { line = it } != null) {
                out += line
                out += "\n"
            }
        }
        process.destroy()
        return out
    }
}