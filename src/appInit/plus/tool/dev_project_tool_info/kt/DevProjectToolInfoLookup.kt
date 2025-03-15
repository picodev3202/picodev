@Suppress("ConstPropertyName")
object DevProjectToolInfoLookup {
    private const val fileWithValueOfToolIde = "file_from_app_by_script_tool_ide"
    private const val toolInfoPath = "${DevProject.wwgen}/lnk/tool_info/"

    fun toolValueIn(devProject: DevProject, fileWithValue: String) =
        devProject.rootPlace.place(toolInfoPath + fileWithValue).readText().trim()

    fun toolInfoIn(devProject: DevProject): ToolInfo {
        val ideAppPath = toolValueIn(devProject, fileWithValueOfToolIde)
        val ideAppStartScriptContent = try {
            LocalPlace.localFile(ideAppPath).takeIf { it.exists() }?.readText() ?: ""
        } catch (_: Exception) {
            ""
        }
        val template = """IDE_CACHE_DIR="${'$'}{XDG_CACHE_HOME:-${'$'}{HOME}/.cache}/"""
        var appVendor = ""
        var appVersion = ""
        if (ideAppStartScriptContent.isNotBlank()) {
            for (line in ideAppStartScriptContent.lines()) {
                if (line.contains(template)) {
                    val array = line.split(template)
                    if (array.size > 1) {
                        val str = array[1].replace('"'.toString(), "").trim()
                        val versionArray = str.split("/")
                        if (versionArray.size == 2) {
                            appVendor = versionArray[0]
                            appVersion = versionArray[1]
                            break
                        }
                    }
                }
            }
        }

        return ToolInfo(
            ideAppPath = ideAppPath,
            appVendor = appVendor,
            appVersion = appVersion
        )
    }
}