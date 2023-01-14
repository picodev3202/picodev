object Tmp2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val devProject = args.firstOrNull()?.let { LocalFile(it) }?.takeIf { it.exists() }
            ?.run { DevProject(RootPlace.lookupToParentOf(absoluteFile)) }
            ?: DevProject.lookupFromCurrentDir()

        println("Tmp2.main ${devProject.rootPlace.file}")
        println("Tmp2.main ${devProject.rootPlace.file("..")}")

        val newDevProject = DevProject(RootPlace(devProject.rootPlace.file("../pico_dev_clean")))
        val newName = "PiCoDevClean"
        println("Tmp2.main ${newDevProject.rootPlace.file}")
        println("Tmp2.main ${newDevProject.fileInRootPlace(DevProject.mainDescLocalPath)}")

        newDevProject.fileInRootPlace(newName).mkdirs()
        devProject.fileInRootPlace(DevProject.mainDescLocalPath)
            .parentFile.copyRecursively(
                newDevProject.fileInRootPlace(DevProject.mainDescLocalPath)
                    .parentFile
            )
        newDevProject.fileInRootPlace(DevProject.mainDescLocalPath).writeText(newName)

        devProject.fileInRootPlace(devProject.name).copyRecursively(newDevProject.fileInRootPlace(newName))

        listOf(
            "appInit",
        ).forEach {
            devProject.fileInSrcPlace(it).copyRecursively(newDevProject.fileInSrcPlace(it))
        }
    }
}