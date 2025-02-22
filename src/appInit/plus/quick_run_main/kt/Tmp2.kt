object Tmp2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val devProject = DevProjectLookup.by(args)

        println("Tmp2.main ${devProject.rootStore.file}")
        println("Tmp2.main ${devProject.rootStore.file("..")}")

        val newDevProject = DevProject(RootStore(devProject.rootStore.place("../pico_dev_clean1")))
        val newName = "PiCoDevClean1"
        println("Tmp2.main ${newDevProject.rootStore.file}")
        println("Tmp2.main ${newDevProject.rootStore.file(DevProject.mainDescLocalPath)}")

        newDevProject.rootStore.file(newName).mkdirs()
        devProject.rootStore.file(DevProject.mainDescLocalPath)
            .parentFile.copyRecursively(
                newDevProject.rootStore.file(DevProject.mainDescLocalPath)
                    .parentFile
            )
        newDevProject.rootStore.file(DevProject.mainDescLocalPath).writeText(newName)

        devProject.rootStore.file(devProject.name).copyRecursively(newDevProject.rootStore.file(newName))

        listOf(
            "appInit",
        ).forEach {
            devProject.src.file(it).copyRecursively(newDevProject.src.file(it))
        }
    }
}