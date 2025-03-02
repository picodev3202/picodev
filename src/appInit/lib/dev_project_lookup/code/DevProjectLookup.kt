object DevProjectLookup {

    fun fromCurrentDir(): DevProject {
        val currentDir = LocalPlace.of(LocalPlace.localFile("").absoluteFile)
        return DevProject(lookupToParentOf(currentDir))
    }

    fun by(args: Array<String>): DevProject {
        return args.firstOrNull()?.let { LocalPlace.of(it) }?.takeIf { it.exists() }
            ?.let { DevProject(lookupToParentOf(it)) }
            ?: fromCurrentDir()
    }

    private fun lookupToParentOf(place: LocalPlace, lookupMarker: String = DevProject.mainDescLocalPath): LocalPlace {
        val lookupPlace = lookupToParentByName(place, lookupMarker)
        if (place.isEmpty) TODO()
        return lookupPlace
    }

    private fun lookupToParentByName(currentPlace: LocalPlace, lookupName: String): LocalPlace {
        if (currentPlace.isEmpty) return LocalPlace.empty
        if (currentPlace.file.isDirectory && currentPlace.place(lookupName).exists()) return currentPlace
        return lookupToParentByName(currentPlace.parent, lookupName)
    }
}