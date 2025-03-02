interface LocalPlace {
    open class From(localPlace: LocalPlace) : Impl(localPlace.file)
    open class Impl(override val file: java.io.File) : Abstract()
    abstract class Abstract() : LocalPlace {
        override fun toString(): String = path
    }

    abstract class AbstractLocalPlace() : Abstract() {
        override val file get() = place.file
        abstract val place: LocalPlace
    }

    companion object {
        private const val DEBUG_LOGS = true
        private inline fun debugLogs(getText: () -> String) = if (DEBUG_LOGS) println(getText()) else Unit
        val empty = Impl(localFile(""))
        val separatorChar = java.io.File.separatorChar
        fun of(file: java.io.File) = Impl(file)
        fun of(path: String) = Impl(localFile(path))
        fun localFile(path: String): java.io.File = java.io.File(path)
    }

    val name: String get() = file.name
    val nameWithoutExtension: String get() = file.nameWithoutExtension
    val file: java.io.File
    fun file(relativePath: String) = java.io.File(file, relativePath).absoluteFile ?: TODO()
    val path: String get() = file.absolutePath ?: TODO()
    fun path(relativePath: String): String = file(relativePath).absolutePath
    fun place(relativePath: String): LocalPlace = of(file(relativePath))
    fun readText(): String = run { if (exists()) file.readText() else "" }
    fun mkdirs(): Boolean = file.mkdirs()

    fun exists() = !isEmpty && file.exists()
    val isEmpty get() = this == empty
    val parent: LocalPlace get() = takeIf { it.exists() }?.file?.parentFile?.let { Impl(it) } ?: empty

    fun deleteIfExist(): Boolean {
        if (exists()) with(file) {
            if (isDirectory) {
                deleteRecursively()
            } else {
                delete()
            }
            debugLogs { "remove: $absolutePath" }
            return true
        }
        return false
    }

    //@Suppress("NOTHING_TO_INLINE")
    /*inline*/ infix fun /*LocalPlace.*/update(text: String): Unit = if (!file.exists()) create { text } else if (file.readText() != text) {
        file.parentFile.mkdirs()
        file.writeText(text)
        debugLogs { "update:           file:///${file.absolutePath}" }
    } else debugLogs { "content the same: file:///${file.absolutePath}" }

    /*inline*/ infix fun /*LocalPlace.*/create(getText: () -> String): Unit = if (!file.exists()) {
        val str = getText()
        if (str.isNotEmpty()) {
            file.parentFile.mkdirs()
            file.writeText(str)
            debugLogs { "create:           file:///${file.absolutePath}" }
        } else debugLogs { "nothing to write: file:///${file.absolutePath}" }
    } else debugLogs { "already exists:   file:///${file.absolutePath}" }

}