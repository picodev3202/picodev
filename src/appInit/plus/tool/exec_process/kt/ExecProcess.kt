@Suppress(
    "unused",
    "MemberVisibilityCanBePrivate",
)
object ExecProcess {

    class Process(private val processBuilder: ProcessBuilder, private val process: java.lang.Process) {

        fun waitOutput(): ProcessOutput {
            var out = ""
            java.io.BufferedReader(java.io.InputStreamReader(process.inputStream)).use { input ->
                var line: String?
                while (input.readLine().also { line = it } != null) {
                    out += line
                    out += "\n"
                }
            }
            process.destroy()
            return ProcessOutput(out)
        }

    }

    class ProcessOutput(private val outStr: String) {
        override fun toString(): String {
            return outStr
        }
    }

    val String.exec get() = exec { }
    fun String.exec() = exec { }
    fun String.exec(block: ProcessBuilder.() -> Unit): Process {
        // println(this)
        val processBuilder = ProcessBuilder("sh", "-c", this)
        processBuilder.block()
        val process = processBuilder.start()
        return Process(processBuilder, process)
    }

    fun ProcessBuilder.dir(place: LocalPlace) {
        directory(place.file)
    }

    fun ProcessBuilder.dir(place: String) {
        directory(place)
    }

    fun ProcessBuilder.directory(place: LocalPlace) {
        directory(place.file)
    }

    fun ProcessBuilder.directory(place: String) {
        directory(java.io.File(place))
    }

}