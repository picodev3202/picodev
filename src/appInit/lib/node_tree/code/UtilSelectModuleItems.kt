object UtilSelectModuleItems : NodeItems() {
    private val util = UtilTree

        class HolderOfModules {
            data class In(val path: ModulePath, val itemContent: NodeItemsDesc.ItemContent, val fullPath: NodePath)
            data class WithDetectedDependency(val itemIn: In, val dependency: List<ModulePath>)
            data class WithDetectedAllDependency(val itemIn: In, val dependency: List<ModulePath>, val allDependency: List<ModulePath>)
            data class Out(
                val path: ModulePath,
                val module: NodeItemsDesc.ItemContent,
                val dependency: List<NodePath>,
                val allDependency: List<ModulePath>,
                val copyContentOf: NodePath
            )

            private val map = mutableMapOf<NodeItemsDesc.ItemContent, In>()
            private val items = mutableListOf<In>()
            private val flatMap = mutableMapOf<String, WithDetectedDependency>()

            fun put(module: NodeItemsDesc.ItemContent, path: List<String>, fullPath: List<String>) =
                put(In(ModulePath(path), module, NodePath(fullPath)))

            private fun put(item: In) {
                map[item.itemContent] = item
                items.add(item)
            }

            private fun get(module: NodeItemsDesc.ItemContent): In {
                return map[module] ?: TODO("Error 3")
            }

            private fun fillDepModuleNode(all: MutableSet<ModulePath>, childrenIdx: List<NodePath>) {
                for (c in childrenIdx) {
                    val item = flatMap[c.fs] ?: TODO("Error 4 ${c.fs}")
                    all.add(item.itemIn.path)
                    fillDepModuleNode(all, item.dependency)
                }
            }

            fun forEach(action: (Out) -> Unit) {
                val itemsWithDetectedDependency = items.map { itemIn ->
                    WithDetectedDependency(itemIn, itemIn.itemContent.dependency.map { get(it).path })
                        .also { flatMap[itemIn.path.fs] = it }
                }

                val itemsWithDetectedAllDependency = itemsWithDetectedDependency.map { item ->
                    val allDeps: MutableSet<ModulePath> = mutableSetOf()
                    fillDepModuleNode(allDeps, item.dependency)
                    WithDetectedAllDependency(item.itemIn, item.dependency, allDeps.sortedBy { it.fs })
                }

                itemsWithDetectedAllDependency.forEach {
                    action(
                        Out(it.itemIn.path,
                            it.itemIn.itemContent,
                            it.dependency,
                            it.allDependency,
                            it.itemIn.itemContent.contentOf?.let { from ->
                                if (it.itemIn.itemContent.isReallyCopy) get(from).path else null
                            } ?: ModulePath.Empty
                        )
                    )
                }
            }
        }

        class SelectorOfItems : NodeItemsDesc(){
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
                    println("fillHolderOfItems mainNode=$mainNode")
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