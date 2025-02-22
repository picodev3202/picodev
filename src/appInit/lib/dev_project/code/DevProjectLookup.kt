object DevProjectLookup {
    fun fromCurrentDir(): DevProject {
        return DevProject(RootStore.lookupFromCurrentDir(DevProject.mainDescLocalPath))
    }

    fun by(args: Array<String>): DevProject {
        return args.firstOrNull()?.let { LocalPlace.of(it) }?.takeIf { it.exists() }
            ?.let { DevProject(RootStore.lookupToParentOf(it)) }
            ?: fromCurrentDir()
    }
}