object ScriptTemplateDebug {
    @JvmStatic
    fun main(args: Array<String>) {
        val tool = ScriptTemplate_Tool()
        val scriptTemplateUtilObj = ScriptTemplateUtil()

        val scriptTemplateFile = tool.file("...")
        val outFile = tool.file("...")

        val templateRelativePath = "app_simple_by_gradle"
        val srcRootsOfSimpleApp = listOf("../plus/app2")

        val res = scriptTemplateUtilObj.scriptTextFromTemplate(scriptTemplateFile.absolutePath, templateRelativePath, srcRootsOfSimpleApp)
        res.takeIf { it.isNotEmpty() }?.let { outFile.apply { parentFile.mkdirs(); println(absoluteFile) }.writeText(it) }

        println("ScriptTemplateDebug.main")
    }
}