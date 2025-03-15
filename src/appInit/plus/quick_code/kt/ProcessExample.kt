object ProcessExample {
    @JvmStatic
    fun main(args: Array<String>) = with(ExecProcess) {
        print("pwd".exec().waitOutput())
        print("ls -la".exec().waitOutput())

        print("pwd".exec { directory("..") }.waitOutput())
        print("pwd".exec { dir("..") }.waitOutput())
        val process = "ls -la ".exec { dir("..") }
        print(process.waitOutput())

        val devProject = DevProjectLookup.by(args)
        devProject.genTmp.takeIf { it.exists() }?.let { println("ls -l".exec { dir(it) }.waitOutput()) }

        println("ls -l".exec { directory(devProject.src) }.waitOutput())

        devProject.genTmp.place("tmp1.txt") update " "
    }
}