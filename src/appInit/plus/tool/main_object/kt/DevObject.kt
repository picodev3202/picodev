abstract class DevObject : MainObject() {
    val logTag by lazy { detectLogTag() }
    val thisFile by lazy { lookupSrcFileByClassName() }
    // val devProject by lazy { DevProjectLookup.by(thisFile) } // val devProject = DevProjectLookup.by(thisFile)
}