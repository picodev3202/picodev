object Tmp {
    @JvmStatic
    fun main(args: Array<String>) {
        val devProject = args.firstOrNull()?.let { LocalFile(it) }?.takeIf { it.exists() }
            ?.run { DevProject(RootPlace.lookupToParentOf(absoluteFile)) }
            ?: DevProject.lookupFromCurrentDir()

        val resStr = "ls -l".exec()
        println("Tmp.main $resStr")

        val resStr2 = "ls -l".exec {
            directory(devProject.srcPlace)
        }
        println("Tmp.main $resStr2")
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