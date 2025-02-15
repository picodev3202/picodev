interface LocalPlace {
    class Impl(override val file: LocalFile) : Abstract()
    abstract class Abstract() : LocalPlace {
        override fun toString(): String = path
    }

    abstract class AbstractLocalPlace() : Abstract() {
        override val file get() = place.file
        abstract val place: LocalPlace
    }

    companion object {
        private const val DEBUG_LOGS = true
        private fun debugLogs(str: String) = if (DEBUG_LOGS) println(str) else Unit

        val empty = Impl(LocalFile(""))
        val separatorChar = LocalFile.separatorChar
        fun of(file: LocalFile) = Impl(file)
        fun of(path: String) = Impl(LocalFile(path))
        fun localFile(path: String): LocalFile = LocalFile(path)
    }

    val name: String get() = file.name
    val file: LocalFile
    fun file(relativePath: String): LocalFile = LocalFile(file, relativePath).absoluteFile
    val path: String get() = file.absolutePath
    fun path(relativePath: String): String = file(relativePath).absolutePath
    fun place(relativePath: String): LocalPlace = of(file(relativePath))

    fun exists() = !isEmpty && file.exists()
    val isEmpty get() = this == empty
    val parent: LocalPlace
        get() {
            if (isEmpty) return empty
            val parentFile = file.parentFile
            if (null == parentFile) return empty
            return Impl(parentFile)
            return file.parentFile?.let { Impl(it) } ?: empty
        }

    /*@Suppress("NOTHING_TO_INLINE")*/
    /*inline*/ infix fun /*LocalPlace.*/update(text: String): Unit = if (!file.exists()) create { text } else if (file.readText() != text) {
        file.parentFile.mkdirs()
        file.writeText(text)
        debugLogs("update:           file:///${file.absolutePath}")
    } else debugLogs("content the same: file:///${file.absolutePath}")

    /*inline*/ infix fun /*LocalPlace.*/create(getText: () -> String): Unit = if (!file.exists()) {
        val str = getText()
        if (str.isNotEmpty()) {
            file.parentFile.mkdirs()
            file.writeText(str)
            debugLogs("create:           file:///${file.absolutePath}")
        } else debugLogs("nothing to write: file:///${file.absolutePath}")
    } else debugLogs("already exists:   file:///${file.absolutePath}")

}