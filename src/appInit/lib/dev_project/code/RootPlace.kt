@Suppress("MemberVisibilityCanBePrivate", "unused")
class RootPlace(val file: LocalFile) {
    companion object {
        const val defaultLookupMarker = DevProject.mainDescLocalPath

        fun lookupFromCurrentDir(lookupMarker: String = defaultLookupMarker): RootPlace {
            val currentDir = LocalFile("")
            return lookupToParentOf(currentDir.absoluteFile, lookupMarker)
        }

        fun lookupToParentOf(place: LocalFile, lookupMarker: String = defaultLookupMarker): RootPlace {
            val rootPlace = lookupToParentByName(place, lookupMarker)
                ?: TODO()
            return RootPlace(rootPlace)
        }

        private fun lookupToParentByName(file: LocalFile?, lookupName: String): LocalFile? {
            if (file == null) return null
            if (file.isDirectory && LocalFile(file, lookupName).exists()) return file
            return lookupToParentByName(file.absoluteFile.parentFile, lookupName)
        }
    }

    fun file(path: String): LocalFile = LocalFile(file, path).absoluteFile

    override fun toString(): String = file.absolutePath
}