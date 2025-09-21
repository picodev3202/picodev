import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup

object ReadNoteBook : MainObject() {

    override fun main(args: Args) {
        println("$objectName.main " + args.devProject.name)
        println("$objectName.main " + args.devProject.rootPlace)

        val notebook = args.devProject.src.place("appInit/plus/tool/char_transform_debug/kt/debug.ipynb")
        println("$objectName.main $notebook")

        val mapper = ObjectMapper()
        val map = mapper.readValue(notebook.readText(), object : TypeReference<MutableMap<String, Any?>>() {})

        println("$objectName.main $map")
        println("$objectName.main " + Jsoup.parse("<html/>"))
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}