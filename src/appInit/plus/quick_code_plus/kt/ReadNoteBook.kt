import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object ReadNoteBook : MainObject() {

    fun noteBookCellValue(rawValue: Any?): String {
        if (rawValue is String) {
            return rawValue
        } else if (rawValue is List<*>) {
            return rawValue.joinToString("") { it.toString() }
        }
        return ""
    }

    class CellSourceValueByPropertyName() {
        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): String {
            var result = ""
            if (thisRef is Node) {
                result = noteBookCellValue(thisRef.otherProperties[property.name])
                // data class Debug(val _source: Any, val _sourceRaw: Any?)
                // thisRef._debug = Debug(result, thisRef.otherProperties["source"])
            }
            return result
        }
    }

    abstract class Node {
        // @Suppress("PropertyName")  var _debug = Any()

        @JsonIgnore
        val otherProperties = mutableMapOf<String, Any?>()

        @JsonAnySetter
        @Suppress("unused")
        fun setOtherProperty(name: String, value: Any?) {
            otherProperties[name] = value
        }
    }

    class NoteBook : Node() {
        class Cell : Node() {
            val source: String by CellSourceValueByPropertyName()

            @JsonProperty("cell_type")
            val cellType = ""

            override fun toString(): String {
                return cellType
            }
        }

        val cells = listOf<Cell>()
    }

    override fun main(args: Args) {
        println("$objectName.main " + args.devProject.name)
        println("$objectName.main " + args.devProject.rootPlace)

        val notebook = args.devProject.src.place("appInit/plus/tool/char_transform_debug/kt/debug.ipynb")
        println("$objectName.main ${notebook.path}")

        val notebookText = notebook.readText()

        val mapper = ObjectMapper()
        val map = mapper.readValue(notebookText, object : TypeReference<Map<String, Any?>>() {})
        val notebookContent = mapper.readValue(notebookText, NoteBook::class.java)
        // notebookContent.cells.forEach { it.source }

        println("$objectName.main $map")
        println("$objectName.main $notebookContent")
    }

    @JvmStatic
    fun main(args: Array<String>) = MainObject(this, args)
}