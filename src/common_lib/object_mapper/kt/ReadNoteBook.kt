import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KProperty

object ReadNoteBook {
    fun noteBookCellValue(rawValue: Any?): String {
        if (rawValue is String) {
            return rawValue
        } else if (rawValue is List<*>) {
            return rawValue.joinToString("") { it.toString() }
        }
        return ""
    }

    class CellSourceVal() {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
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
        @Suppress("unused", "EnumEntryName")
        enum class CellType { markdown, code }

        class Cell : Node() {

            val source: String by CellSourceVal()

            @JsonProperty("cell_type")
            val cellType = ""

            override fun toString(): String = cellType
        }

        val cells = listOf<Cell>()
    }

    fun notebookContent(notebookText: String): NoteBook {

        val mapper = ObjectMapper()
        val map = mapper.readValue(notebookText, object : TypeReference<Map<String, Any?>>() {})
        val notebookContent = mapper.readValue(notebookText, NoteBook::class.java)
        // notebookContent.cells.forEach { it.source }
        return notebookContent
    }

}