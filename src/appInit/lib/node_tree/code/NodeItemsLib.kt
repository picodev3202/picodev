abstract class NodeItemsLib {

    class LibraryContent(override val name: String, override val nodeFrom: Any) : NodeItemsDesc.ItemContent, NodeItemWithName {
        override val dependency: List<NodeItemsDesc.ItemContent> = emptyList()
        override val contentOf: NodeItemsDesc.ItemContent? = null
        override val type = NodeItemsDesc.Content.Type.Empty
        override val isReallyCopy: Boolean = false
        override val renameTo: String = ""
    }
}