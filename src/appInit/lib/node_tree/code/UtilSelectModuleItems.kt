@Suppress("RemoveRedundantQualifierName")
object UtilSelectModuleItems : NodeItems() {
    private val util = UtilTree

    class HolderOfModules {
        data class In(val path: NodePath, val itemContent: NodeItemsDesc.ItemContent, val fullPath: NodePath)
        data class WithDetectedDependency(val itemIn: In, val dependency: List<NodePath>, val libraries: List<LibraryContent>)
        data class WithDetectedAllDependency(val itemIn: In, val dependency: List<NodePath>, val allDependency: List<NodePath>, val libraries: List<LibraryContent>)
        data class Out(
            val path: NodePath,
            val module: NodeItemsDesc.ItemContent,
            val dependency: List<NodePath>,
            val allDependency: List<NodePath>,
            val libraries: List<LibraryContent>,
            val copyContentOf: NodePath?,
            val renameTo: String?,
        )

        private val map = mutableMapOf<NodeItemsDesc.ItemContent, In>()
        private val items = mutableListOf<In>()
        private val flatMapOfDependency = mutableMapOf<String, WithDetectedDependency>()

        fun put(module: NodeItemsDesc.ItemContent, path: List<String>, fullPath: List<String>) =
            put(In(NodePath(path), module, NodePath(fullPath)))

        private fun put(item: In) {
            map[item.itemContent] = item
            items.add(item)
        }

        private fun get(module: NodeItemsDesc.ItemContent): In {
            return map[module] ?: TODO("Error 3")
        }

        private fun fillDepModuleNode(all: MutableSet<NodePath>, childrenIdx: List<NodePath>) {
            for (c in childrenIdx) {
                val item = flatMapOfDependency[c.fs] ?: TODO("Error 4 ${c.fs}")
                all.add(item.itemIn.path)
                fillDepModuleNode(all, item.dependency)
            }
        }

        fun itemsWithDetectedAllDependency(ignoreDep: List<NodeItemsDesc.ItemContent> = emptyList()): List<WithDetectedAllDependency> {
            val itemsWithDetectedDependency = items.map { itemIn ->
                val dependency = mutableListOf<NodePath>()
                val libraries = mutableListOf<LibraryContent>()
                itemIn.itemContent.dependency.forEach { if (it is LibraryContent) libraries.add(it) else { val depItemIn: In = get(it); if (depItemIn.itemContent !in ignoreDep) { dependency.add(depItemIn.path) } } }
                WithDetectedDependency(itemIn, dependency, libraries)
                    .also { flatMapOfDependency[itemIn.path.fs] = it }
            }

            return itemsWithDetectedDependency.map { item ->
                val allDeps: MutableSet<NodePath> = mutableSetOf()
                fillDepModuleNode(allDeps, item.dependency)
                WithDetectedAllDependency(item.itemIn, item.dependency, allDeps.sortedBy { it.fs }, item.libraries)
            }
        }

        fun forEachSmart(predicate: (Out) -> Boolean = { true }, action: (Out) -> Unit) = itemsWithDetectedAllDependency().forEach {
            Out(
                it.itemIn.path,
                it.itemIn.itemContent,
                it.dependency,
                it.allDependency,
                it.libraries,
                it.itemIn.itemContent.contentOf?.let { from ->
                    if (it.itemIn.itemContent.isReallyCopy) get(from).path else null
                },
                it.itemIn.itemContent.renameTo,
            ).takeIf(predicate)?.let(action)
        }
    }

    class SelectorOfItems : NodeItemsDesc() {
        private val holder = HolderOfModules()
        private var lastSrcPlace: Node.SrcPlace? = null
        private fun onItem(srcPlace: Node.SrcPlace, path: List<String>, module: ItemContent, fullPath: List<String>) {
            val lastSrcPlc = lastSrcPlace
            if (null == lastSrcPlc) {
                lastSrcPlace = srcPlace
            } else {
                //assertEquals(lastSrcPlc, srcPlace) { "In this case 'srcPlace' must be only one" }
                if (lastSrcPlc != srcPlace) throw AssertionError("In this case 'srcPlace' must be only one")
            }
            holder.put(module, path, fullPath)
        }

        fun <MainObj : NodeItemsDesc.Node> fillHolderOfItems(mainObj: MainObj): HolderOfModules {
            val mainObjectsTree = util.objectsTree(mainObj, NodeItemsDesc.Node.Unit)
            mainObjectsTree.run {
                // println("fillHolderOfItems mainNode=$mainNode")
                if (mainObj is Node.SrcPlace) mainNode.children.forEach<NodeItemsDesc.Node.Item> { path, _, obj, fullPath ->
                    onItem(mainObj, path, itemContent(obj), fullPath)
                }
                else mainNode.children.forEach<SrcPlace> { _, srcPlaceNode, srcPlace, _ ->
                    srcPlaceNode.children.forEach<NodeItemsDesc.Node.Item> { path, _, obj, fullPath ->
                        onItem(srcPlace, path, itemContent(obj), fullPath)
                    }
                }
            }
            return holder
        }
    }

    fun <MainObj : NodeItemsDesc.Node> select(mainObj: MainObj) = SelectorOfItems().fillHolderOfItems(mainObj)
}