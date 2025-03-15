class ToolRunPlace(private val devProject: DevProject) : LocalPlace.From(devProject.rootPlace.place(toolRunStorePath)) {
    val isValid get() = place("file_from_app_by_script").readText().trim() == devProject.rootPlace.path(DevProject.wwgen)

    companion object {
        @Suppress("ConstPropertyName")
        private const val toolRunStorePath = "${DevProject.wwgen}/lnk/tool_run_store/"
        fun from(devProject: DevProject): ToolRunPlace {
            return ToolRunPlace(devProject)
        }
    }
}