data class ToolInfo(
    val ideAppPath: String,
    val appVendor: String,
    val appVersion: String,
) {
    @Suppress("ConstPropertyName")
    companion object {
        const val scratches = "/scratches/"
        const val consolesIde = "consoles/ide"
        const val defaultIdeScript = "ide-scripting.kts"
        const val defaultIdeScriptContent = """//
KotlinVersion.CURRENT
"""
        const val ideScripting = "$consolesIde/$defaultIdeScript"
    }
}