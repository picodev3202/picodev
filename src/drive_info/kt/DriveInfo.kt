import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*


object DriveInfo {

    class Raw {
        @Suppress("unused")
        abstract class Abstract {
            @JsonIgnore
            val otherProperties = mutableMapOf<String, Any?>()

            @JsonAnySetter
            fun setOtherProperty(name: String, value: Any?) {
                otherProperties[name] = value
            }
        }

        sealed class Item : Abstract() {
            val id = ""
            val name = ""
            val url = ""

            override fun toString(): String = name
        }

        class Folder : Item() {
            val folders = listOf<Folder>()
            val files = listOf<File>()
        }

        class File : Item() {
            class Doc : Abstract() {
                val name = ""
                val url = ""
                val text = ""

                companion object {
                    val empty = Doc()
                }
            }

            val size = 0
            val mimeType = ""
            val doc = Doc.empty
        }

    }

    class Smart() {
        class Item(
            val parents: List<Folder>,
            val id: String,
            val name: String,
            val url: String,
            val rawItem: Raw.Item
        )

        sealed class FileItem(item: Item) {
            @Suppress("PropertyName")
            val _raw = item.rawItem

            @Suppress("PropertyName")
            val _parents = item.parents
            val parents = item.parents.map { it.name }
            val parentsStr = this.parents.joinToString("/")
            val name = item.name
            val id = item.id
            val url = item.url
            override fun toString(): String = "$name      (${"$parentsStr/$name"})"
        }

        class Folder(item: Item) : FileItem(item) {
            val items = LinkedList<FileItem>()
            override fun toString(): String = "$name (${items.size})  (${"$parentsStr/$name"})"
        }

        open class File(val mimeType: String, item: Item) : FileItem(item)

        class Doc(val docName: String, val text: String, mimeType: String, item: Item) : File(mimeType, item)

    }

    class Tool() {
        val mapOfFolders = mutableMapOf<String, Smart.Folder>()

        fun rawFileToSmartFile(rawFile: Raw.File, parents: List<Smart.Folder>): Smart.File {
            val item = Smart.Item(parents, rawFile.id, rawFile.name, rawFile.url, rawFile)
            return if (rawFile.doc != Raw.File.Doc.empty) {
                Smart.Doc(rawFile.doc.name, rawFile.doc.text, rawFile.mimeType, item)
            } else {
                Smart.File(rawFile.mimeType, item)
            }
        }

        fun rawFolderToSmartFolder(rawFolder: Raw.Folder, parents: List<Smart.Folder> = emptyList()): Smart.Folder {
            val item = Smart.Item(parents, rawFolder.id, rawFolder.name, rawFolder.url, rawFolder)
            val folder = Smart.Folder(item);
            mapOfFolders[folder.id] = folder
            val parentsNew = mutableListOf(*parents.toTypedArray(), folder)
            rawFolder.folders.forEach { folder.items.add(rawFolderToSmartFolder(it, parentsNew)) }
            rawFolder.files.forEach { folder.items.add(rawFileToSmartFile(it, parentsNew)) }
            return folder
        }

    }

}
