object CopyOfDevProjectExample : MainObject() {

    override fun main(args: Args): Unit = with(ExecProcess) {
        println("$logTagName.main ${args.array}")
        println("$logTagName.main $objectName")
        println("$logTagName.main ${args.devProject.rootPlace}")
        println("$logTagName.main ${args.devProject.rootPlace.file("..")}")

        val newName = "${args.devProject.name}CleanCopy"
        val newDevProject = DevProject(args.devProject.rootPlace.place("../${args.devProject.rootPlace.name}_clean_copy"))

        println("$logTagName.main ${newDevProject.rootPlace.file}")
        println("$logTagName.main ${newDevProject.rootPlace.file(DevProject.mainDescLocalPath)}")

        newDevProject.rootPlace.file(newName).mkdirs()

        args.devProject.rootPlace.file(DevProject.mainDescLocalPath)
            .parentFile.copyRecursively(newDevProject.rootPlace.file(DevProject.mainDescLocalPath).parentFile)

        newDevProject.rootPlace.file(DevProject.mainDescLocalPath).writeText(newName)

        args.devProject.rootPlace.file(args.devProject.name).copyRecursively(newDevProject.rootPlace.file(newName))

        listOf("plus", "plus1", "plus2", "plus3").forEach {
            args.devProject.rootPlace.file(it).copyRecursively(newDevProject.rootPlace.file(it))
        }

        listOf(
            "appInit",
        ).forEach {
            args.devProject.src.file(it).copyRecursively(newDevProject.src.file(it))
        }

        "chmod +x ${newDevProject.src.path("appInit/app_by_script/open_in_ide")}".exec().waitOutput()
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args) {
        localPathToCurrentSourceFile..""
    }
}
