@file:Suppress("unused")


val tmp02 = ScriptApi.
//
//
//
//
//
//
//
//

buildscript {

    tasks.register("prepare_cache_of_gradle_generated_jar_task") {
        description = "generatedDebugTaskDesk"
        group = "generated"

        @Suppress("FunctionName")
        doLast {
            fun LocalFile(parent: String, child: String) = java.io.File(parent, child)
            val cachesJars = LocalFile(gradle.gradleUserHomeDir.absolutePath, "caches/${gradle.gradleVersion}/generated-gradle-jars/")
            val gradleGeneratedPath = file("arg01.txt").readText().trim()
            val version = "version"
            val gradleGeneratedVersionFile = LocalFile(gradleGeneratedPath, version)
            val gradleGeneratedVersionDir = gradleGeneratedVersionFile.parentFile
            val gradleGeneratedVersionDir0 = LocalFile(gradleGeneratedVersionDir.parent, gradleGeneratedVersionDir.name + "0")
            val version0File = LocalFile(gradleGeneratedVersionDir0.absolutePath, version)
            val version0NamedFile = LocalFile(gradleGeneratedVersionDir0.absolutePath, "${version}.${gradle.gradleVersion}")

            val list = cachesJars.listFiles()?.filter { file -> file?.extension == "jar" }?.map { file -> file!! } ?: emptyList()

            if (gradleGeneratedVersionFile.exists()) {
                gradleGeneratedVersionDir.renameTo(gradleGeneratedVersionDir0)
            }

            val versionText = if (version0File.exists()) version0File.readText().trim() else ""
            if (versionText != gradle.gradleVersion && list.isNotEmpty()) {
                gradleGeneratedVersionDir0.mkdirs()
                if (gradleGeneratedVersionDir0.exists()) {
                    list.forEach { file -> file.copyTo(LocalFile(gradleGeneratedVersionDir0.absolutePath, file.name)) }
                    version0File.writeText(gradle.gradleVersion)
                    version0NamedFile.writeText(gradle.gradleVersion)
                } else {
                    println("directory $gradleGeneratedVersionDir0 is not exists")
                }
            }
            if (gradleGeneratedVersionDir0.exists()) {
                gradleGeneratedVersionDir0.renameTo(gradleGeneratedVersionDir)
            }
        }
    }

}