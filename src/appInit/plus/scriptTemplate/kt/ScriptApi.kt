@Suppress("SpellCheckingInspection", "unused")
object ScriptApi {

    interface Gradle {
        val gradleVersion: String
        val gradleUserHomeDir: java.io.File
    }

    interface Task {
        val name: String
        val path: String
        var description: String
        var group: String
        fun doLast(block: Task.() -> Unit)
    }

    interface Tasks {
        fun register(taskName: String, block: Task.() -> Unit)
    }

    interface Project {
        val gradle: Gradle
        fun file(path: String): java.io.File = java.io.File(path)
        val tasks: Tasks
        fun Project.buildscript(block: ApplyApi.() -> Unit)
    }

    interface ApplyApi {
        fun ApplyApi.apply(from: String)
    }

    interface BuildScriptNodeApi : Project, ApplyApi

    fun allprojects(block: Project.() -> Unit) = Unit
    fun buildscript(block: BuildScriptNodeApi.() -> Unit) = Unit
}