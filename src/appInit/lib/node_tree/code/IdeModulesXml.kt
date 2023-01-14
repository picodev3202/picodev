object IdeModulesXml : NodeItems() {

    fun modulesXml(modules: List<String>): String {
        val modulesPlace = mutableListOf<String>()
        for (moduleRelativePath in modules) {
            modulesPlace.add("""      <module fileurl="file://${'$'}PROJECT_DIR${'$'}/$moduleRelativePath" filepath="${'$'}PROJECT_DIR${'$'}/$moduleRelativePath" />""")
        }

        return """<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectModuleManager">
    <modules>
      <module fileurl="file://${'$'}PROJECT_DIR${'$'}/modules/_0/all_sources.iml" filepath="${'$'}PROJECT_DIR${'$'}/modules/_0/all_sources.iml" />
      <module fileurl="file://${'$'}PROJECT_DIR${'$'}/modules/_0/gen_py_envs.iml" filepath="${'$'}PROJECT_DIR${'$'}/modules/_0/gen_py_envs.iml" />
      <module fileurl="file://${'$'}PROJECT_DIR${'$'}/modules/_0/generated_files.iml" filepath="${'$'}PROJECT_DIR${'$'}/modules/_0/generated_files.iml" />
      <module fileurl="file://${'$'}PROJECT_DIR${'$'}/modules/_0/project_config.iml" filepath="${'$'}PROJECT_DIR${'$'}/modules/_0/project_config.iml" />

${modulesPlace.joinToString("\n")}
    </modules>
  </component>
</project>"""
    }


    val moduleXmlKt = fun(contentUrl: String, srcsUrls: List<String>, deps: List<String>) = """<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="3">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content $contentUrl">
${srcsUrls.joinToString("\n")}
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
    <orderEntry type="library" name="KotlinJavaRuntime" level="project" />
${deps.joinToString("") { "$it\n" }}  </component>
</module>"""

    val moduleXmlJv = fun(contentUrl: String, srcsUrls: List<String>, deps: List<String>) = """<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content $contentUrl">
${srcsUrls.joinToString("\n")}
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
${deps.joinToString("") { "$it\n" }} </component>
</module>"""

    val moduleXmlGeneral = fun(contentUrl: String, srcsUrls: List<String>, deps: List<String>) = """<?xml version="1.0" encoding="UTF-8"?>
<module type="GENERAL_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content $contentUrl">
    </content>
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>"""

    val moduleXmlBoardCode = fun(contentUrl: String, srcsUrls: List<String>, deps: List<String>) = """<?xml version="1.0" encoding="UTF-8"?>
<module type="GENERAL_MODULE" version="4">
  <component name="FacetManager">
    <facet type="Python" name="Python">
      <configuration sdkName="Python 3 (PiCo venv_cp)" />
    </facet>
  </component>
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content $contentUrl">
${srcsUrls.joinToString("\n")}
    </content>
    <orderEntry type="sourceFolder" forTests="false" />
    <orderEntry type="library" name="Python 3 (PiCo venv_cp) interpreter library" level="application" />
${deps.joinToString("") { "$it\n" }}  </component>
</module>"""

    fun moduleGeneric(
        prjName: String,
        place: LocalFile,
        srcStr: String,
        path: ModulePath,
        moduleItemType: DefaultTypes.ItemType,
        deps: List<ModulePath>
    ): String {
        val pathStr = path.fs
        val urlBegin = """url="file://${'$'}MODULE_DIR${'$'}/../../../../$srcStr/"""
        val contentUrl = "$urlBegin$pathStr"
        val srcsUrls = mutableListOf<String>()
        for (srcOne in moduleItemType.srcs) {
            val (srcRaw, example) = srcOne
            val src = srcRaw.trim()
            val scrDir = LocalFile(place, "$srcStr/$pathStr/$src")
            if (!scrDir.exists()) {
                scrDir.mkdirs()
                if (moduleItemType.withExample) {
                    val (nm, content) = example
                    LocalFile(scrDir, nm).apply {
                        writeText(content(nameWithoutExtension, "from $nameWithoutExtension $pathStr"))
                    }
                }
            }
            val url = "$urlBegin$pathStr/$src"
            srcsUrls.add("""      <sourceFolder $url" isTestSource="false" />""")
        }

        val moduleRelativePath = "modules/gen/${path.list[0]}/${path.ide}.iml"
        val moduleFile = LocalFile(place, "$prjName/$moduleRelativePath")
        println("IdeModulesXml.moduleGeneric $moduleFile")
        moduleFile.apply { parentFile.mkdirs() }
            .writeText(
                moduleItemType.moduleXmlStr(contentUrl, srcsUrls, deps.map { """    <orderEntry type="module" module-name="${it.ide}" />""" })
            )
        return moduleRelativePath
    }
}
