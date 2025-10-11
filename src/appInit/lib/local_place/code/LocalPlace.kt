interface LocalPlace {
    open class From(localPlace: LocalPlace) : Impl(localPlace.file.absoluteFile.canonicalFile)
    open class Impl(override val file: java.io.File) : Abstract()
    abstract class Abstract() : LocalPlace {
        override fun toString(): String = path
    }

    companion object {
        private const val DEBUG_LOGS = true
        private inline fun debugLogs(getText: () -> String) = if (DEBUG_LOGS) println(getText()) else Unit
        val empty: LocalPlace = Impl(localFile(""))
        val separatorChar = java.io.File.separatorChar
        fun of(file: java.io.File): LocalPlace = Impl(file.absoluteFile.canonicalFile)
        fun of(path: String): LocalPlace = Impl(localFile(path).absoluteFile.canonicalFile)
        fun localFile(path: String): java.io.File = java.io.File(path.trim())
    }

    val name: String get() = file.name
    val nameWithoutExtension: String get() = file.nameWithoutExtension
    val file: java.io.File
    fun file(relativePath: String) = java.io.File(file, relativePath).absoluteFile.canonicalFile ?: TODO()
    val path: String get() = file.absolutePath ?: TODO()
    fun path(relativePath: String): String = file(relativePath).absolutePath
    fun place(relativePath: String): LocalPlace = of(file(relativePath))
    fun readText(): String = run { if (exists()) file.readText() else "" }
    fun mkdirs(): Boolean = file.mkdirs()

    fun exists() = !isEmpty && file.exists()
    val isEmpty get() = this == empty
    val parent: LocalPlace get() = takeIf { it.exists() }?.file?.parentFile?.let { Impl(it.absoluteFile.canonicalFile) } ?: empty

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

    infix fun updateSilent(text: String) = if (!file.exists()) createSilent { text } else if (file.readText() != text) {
        file.parentFile.mkdirs()
        file.writeText(text)
    } else Unit

    infix fun createSilent(getText: () -> String) = if (!file.exists()) {
        val str = getText()
        if (str.isNotEmpty()) {
            file.parentFile.mkdirs()
            file.writeText(str)
        } else Unit
    } else Unit


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