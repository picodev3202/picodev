@Suppress("MemberVisibilityCanBePrivate")
object ProcessExample {

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

    @JvmStatic
    fun main(args: Array<String>) {
        print("pwd".exec().waitOutput())
        print("ls -la".exec().waitOutput())

        print("pwd".exec { directory("..") }.waitOutput())
        print("pwd".exec { dir("..") }.waitOutput())
        val process = "ls -la ".exec({ dir("..") })
        print(process.waitOutput())
    }

    fun String.exec() = exec { }
    fun String.exec(block: ProcessBuilder.() -> Unit): Process {
        val processBuilder = ProcessBuilder("sh", "-c", this)
        processBuilder.block()
        val process = processBuilder.start()
        return Process(processBuilder, process)
    }

    fun ProcessBuilder.dir(place: String) {
        directory(place)
    }

    fun ProcessBuilder.directory(place: String) {
        directory(java.io.File(place))
    }

}