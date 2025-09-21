object LoadLib {

    class LibInfo(val link: String, val plusLinks: List<String> = emptyList())

    @JvmStatic
    fun main(args: Array<String>) {
        val devProject = DevProjectLookup.by(args)
        println("LoadLib.main " + devProject.name)
        println("LoadLib.main " + devProject.rootPlace)

        listOf(
            LibInfo("https://repo1.maven.org/maven2/org/jsoup/jsoup/1.21.2/jsoup-1.21.2.jar"),
            LibInfo(
                "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.20.0/jackson-databind-2.20.0.jar",
                listOf(
                    "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.20.0/jackson-core-2.20.0.jar",
                    "https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.20/jackson-annotations-2.20.jar"
                )
            )
        ).forEach { processLib(devProject, it) }
    }

    fun processLib(devProject: DevProject, libLink: LibInfo) {
        class LibItem(link: String) {
            private val uri = java.net.URI(link)
            private val pathHelper = LocalPlace.of(uri.path)
            val fileName = pathHelper.name
            val name = pathHelper.nameWithoutExtension.trim { it.isDigit() || it == '-' || it == '.' }
            fun fetchBytes() = uri.toURL().readBytes()
        }

        fun libPlace(name: String) = DevProject.run { "$wwgen/libs/lib_$name" }
        val libItem = LibItem(libLink.link)

        devProject.rootPlace.place(libPlace(libItem.name)).apply { mkdirs() }.file(libItem.fileName).writeBytes(libItem.fetchBytes())
        libLink.plusLinks.forEach {
            val plusItem = LibItem(it)
            devProject.rootPlace.place(libPlace(libItem.name)).file(plusItem.fileName).writeBytes(plusItem.fetchBytes())
        }

        val content = if (libLink.plusLinks.isEmpty()) {
            """<component name="libraryTable">
  <library name="${libItem.name}">
    <CLASSES>
      <root url="jar://${'$'}PROJECT_DIR$/../${libPlace(libItem.name)}/${libItem.fileName}!/" />
    </CLASSES>
    <JAVADOC />
    <SOURCES />
  </library>
</component>"""
        } else {
            """<component name="libraryTable">
  <library name="${libItem.name}">
    <CLASSES>
      <root url="file://${'$'}PROJECT_DIR$/../${libPlace(libItem.name)}" />
    </CLASSES>
    <JAVADOC />
    <SOURCES />
    <jarDirectory url="file://${'$'}PROJECT_DIR$/../${libPlace(libItem.name)}" recursive="false" />
  </library>
</component>"""
        }

        val libXmlName = libItem.name.replace("-", "_")
        devProject.run { rootPlace.file("${name}/.idea/libraries/${libXmlName}.xml").writeText(content) }
    }
}