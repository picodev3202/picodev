@Suppress("ConstPropertyName")
class RootStore(override val place: LocalPlace) : LocalPlace.AbstractLocalPlace() {
    companion object {
        const val defaultLookupMarker = DevProject.mainDescLocalPath

        fun lookupFromCurrentDir(lookupMarker: String = defaultLookupMarker): RootStore {
            val currentDir = LocalPlace.of(LocalPlace.localFile("").absoluteFile)
            return lookupToParentOf(currentDir, lookupMarker)
        }

        fun lookupToParentOf(place: LocalPlace, lookupMarker: String = defaultLookupMarker): RootStore {
            val rootPlace = lookupToParentByName(place, lookupMarker)
            if (place.isEmpty)  TODO()
            return RootStore(rootPlace)
        }

        private fun lookupToParentByName(currentPlace: LocalPlace, lookupName: String): LocalPlace {
            if (currentPlace.isEmpty) return LocalPlace.empty
            if (currentPlace.file.isDirectory && currentPlace.place(lookupName).exists()) return currentPlace
            return lookupToParentByName(currentPlace.parent, lookupName)
        }
    }
}