object UtilTree : UtilOfJClass.Lib() {

    fun listOfSuperclassDeclaredFields(superclass: Class<*>?, block: (java.lang.reflect.Field?) -> Unit) {
        superclass?.let {
            it.declaredFields.forEach(block)
            listOfSuperclassDeclaredFields(it.superclass, block)
        }
    }

    fun forEachValueOfField(obj: Any?, prev: List<String> = emptyList(), block: (path: List<String>, fieldValue: Any) -> Unit) {
        if (null != obj) {
            val foundNames = mutableSetOf<String>()
            obj.javaClass.declaredFields.forEach { field ->
                if (null != field) {
                    if (!field.name.contains('$') && !foundNames.contains(field.name)) {
                        field.trySetAccessible()
                        val fVal = field.get(obj)
                        if (null != fVal) {
                            val path: List<String> = prev + field.name
                            block(path, fVal)
                            forEachValueOfField(fVal, path, block)
                        }
                    }
                    foundNames.add(field.name)
                }
            }
            listOfSuperclassDeclaredFields(obj.javaClass.superclass) { field ->
                if (null != field) {
                    if (!field.name.contains('$') && !foundNames.contains(field.name)) {
                        field.trySetAccessible()
                        val fVal = field.get(obj)
                        if (null != fVal) {
                            val path: List<String> = prev + field.name
                            block(path, fVal)
                            forEachValueOfField(fVal, path, block)
                        }
                    }
                    foundNames.add(field.name)
                }
            }
        }
    }

    inline fun <MainObj : Node, reified Node : Any> objectsTree(rootObject: MainObj, empty: Node): ObjectsTree<MainObj> {
        val nodeTreeBuilder = NodeTreeBuilder(empty)
        listOfClasses(rootObject.javaClass) {
            val path = it.name.split("$").toList()
            val obj = instantiateObject(it)
            if (obj is Node) {
                nodeTreeBuilder.put(path, obj)
            }
            val foundNames = mutableSetOf<String>()
            it.declaredFields.forEach { field ->
                if (null != field) {
                    if (field.name != propInstance && !field.name.contains('$') && !foundNames.contains(field.name)) {
                        field.trySetAccessible()
                        field.get(null)?.let { fieldVal ->
                            if (fieldVal is Node) {
                                val fieldPath: List<String> = path + field.name
                                nodeTreeBuilder.put(fieldPath, fieldVal)
                                forEachValueOfField(fieldVal, fieldPath) { path, fieldValue ->
                                    if (fieldValue is Node){
                                        nodeTreeBuilder.put(path, fieldValue)
                                    }
                                }
                            }
                        }
                    }
                    foundNames.add(field.name)
                }
            }
            listOfSuperclassDeclaredFields(it.superclass) { field ->
                if (null != field) {
                    if (!field.name.contains('$') && !foundNames.contains(field.name)) {
                        field.trySetAccessible()
                        field.get(obj)?.let { fieldVal ->
                            if (fieldVal is Node) {
                                val fieldPath: List<String> = path + field.name
                                nodeTreeBuilder.put(fieldPath, fieldVal)
                                forEachValueOfField(fieldVal, fieldPath) { path, fieldValue ->
                                    if (fieldValue is Node){
                                        nodeTreeBuilder.put(path, fieldValue)
                                    }
                                }
                            }
                        }
                    }
                    foundNames.add(field.name)
                }
            }
        }

        var rootIndex: NodeOfTree.Index? = null
        val nodeTree = nodeTreeBuilder.build { node, instanceOfObject ->
            if (instanceOfObject == rootObject) rootIndex = node.params.index
        }
        return rootIndex?.let {
            ObjectsTree(rootObject, it, nodeTree.allNodes, nodeTree.values, nodeTree.paths)
        } ?: TODO("index of root object not found")
    }

    fun objectsTree(rootObject: LocalFile): ObjectsTree<LocalFile> {
        val empty = LocalFile("/tmp/empty")
        val nodeTreeBuilder = NodeTreeBuilder(empty)

        rootObject.walk().forEach {
            if (it.isDirectory) {
                val separatorChar = LocalFile.separatorChar
                val path = it.path.trim(separatorChar).split(separatorChar).toList()
                nodeTreeBuilder.put(path, it)
            }
        }
        var rootIndex: NodeOfTree.Index? = null
        val nodeTree = nodeTreeBuilder.build { node, value ->
            if (value == rootObject) rootIndex = node.params.index
        }
        return rootIndex?.let {
            ObjectsTree(rootObject, it, nodeTree.allNodes, nodeTree.values, nodeTree.paths)
        } ?: TODO("index of root object not found")
    }

    class ObjectsTree<R>(
        val main: R,
        private val mainIndex: NodeOfTree.Index,
        nodes: List<NodeOfTree>,
        objects: List<Any>,
        fullPaths: List<List<String>>,
        private val data: DataHolder = DataHolder(nodes, objects, fullPaths)
    ) {
        val mainNode get(): Node = data.node(mainIndex)

        @Suppress("MemberVisibilityCanBePrivate")
        data class DataHolder(val allNodes: List<NodeOfTree>, val objects: List<Any>, val fullPaths: List<List<String>>) {
            companion object {
                val empty = DataHolder(emptyList(), emptyList(), emptyList())
            }

            val nodesMap by lazy { Array(allNodes.size) { Node.undefined } }
            val childrenMap by lazy { Array(allNodes.size) { Children.undefined } }

            fun children(params: NodeOfTree.Params): Children = childrenMap[params.index.value].let {
                if (it == Children.undefined) {
                    val define = if (params.childrenIdx.isEmpty()) Children.empty else Children(params.childrenIdx, this)
                    define.also { childrenMap[params.index.value] = define }
                } else it
            }

            fun node(index: NodeOfTree.Index): Node = nodesMap[index.value].let {
                if (it == Node.undefined) {
                    val node = allNodes[index.value]
                    Node(node.name, node.params, this).apply { nodesMap[index.value] = this }
                } else it
            }
        }

        class Node(val name: String, val params: NodeOfTree.Params, val data: DataHolder) {
            companion object {
                val undefined = Node("", NodeOfTree.empty.params, DataHolder.empty)
                val empty = Node("", NodeOfTree.empty.params, DataHolder.empty)
            }

            val children: Children get() = data.children(params)
            val parent: Node get() = params.parentIdx.let { if (it.value > 0) data.node(it) else empty }

            val path: List<String>
                get() {
                    val res = mutableListOf<String>()
                    res.add(name)
                    var i = parent
                    while (i != empty) {
                        res.add(i.name)
                        i = i.parent
                    }
                    return res.reversed()
                }

            override fun toString(): String {
                return "($name $params)"
            }
        }

        class Children(val childrenIdx: List<NodeOfTree.Index>, val dataHolder: DataHolder) {
            companion object {
                val undefined = Children(emptyList(), DataHolder.empty)
                val empty = Children(emptyList(), DataHolder.empty)

                fun forEachIndexed(
                    childrenIdx: List<NodeOfTree.Index>,
                    dataHolder: DataHolder,
                    prev: List<String> = emptyList(),
                    index: Int = 0,
                    block: (index: Int, path: List<String>, node: Node) -> Unit
                ) {
                    var idx = index
                    for (c in childrenIdx) {
                        val node = dataHolder.node(c)
                        val path = listOf(*prev.toTypedArray(), node.name)
                        block(idx, path, node)
                        idx++
                        forEachIndexed(node.params.childrenIdx, dataHolder, path, idx, block)
                    }
                }
            }

            fun paths(): List<List<String>> {
                val res = mutableListOf<List<String>>()
                forEach { path, _ -> res.add(path) }
                return res
            }

            inline fun <reified T> pathsTmp(): List<List<String>> {
                val res = mutableListOf<List<String>>()
//            forEach<T> { res.add(path) }
                forEach<T> { path, _, _, _ ->
                    res.add(path)
                }
                return res
            }

            fun pathsStr(): List<String> {
                val res = mutableListOf<String>()
                forEach { path, _ -> res.add(path.joinToString("/")) }
                return res
            }

            fun forEach(filter: ((path: List<String>, node: Node) -> Boolean)? = null, block: (path: List<String>, node: Node) -> Unit) {
                forEachIndexed(childrenIdx, dataHolder) { _, path, node -> if (filter?.let { it(path, node) } != false) block(path, node) }
            }

            fun forEachWithChildrenEmpty(block: (path: List<String>, node: Node) -> Unit) {
                forEach(filter = { _, node -> node.params.childrenIdx.isEmpty() }, block)
            }

            fun forEachIndexed(block: (index: Int, path: List<String>, node: Node) -> Unit) {
                forEachIndexed(childrenIdx, dataHolder, block = block)
            }

            inline fun <reified T> forEach(
                basePath: List<String> = emptyList(), crossinline block: (path: List<String>, node: Node, obj: T, fullPath: List<String>) -> Unit
            ) {
                forEachIndexed(childrenIdx, dataHolder, basePath) { _, path, node ->
                    val obj = dataHolder.objects[node.params.index.value]
                    if (obj is T) {
                        val fullPath = dataHolder.fullPaths[node.params.index.value]
                        block(path, node, obj, fullPath)
                    }
                }
            }
        }
    }

    data class NodeOfTree(val name: String, val params: Params) {
        data class Params(val index: Index, val parentIdx: Index, val childrenIdx: List<Index>)
        data class Index(val value: Int) {
            override fun toString(): String {
                return "idx$value"
            }
        }

        companion object {
            val empty = NodeOfTree("", Params(Index(-1), Index(-1), emptyList()))
        }
    }

    class NodeTreeBuilder<T>(emptyValue: T) {
        data class Result<T>(val allNodes: List<NodeOfTree>, val values: List<T>, val paths: List<List<String>>)

        class NodeItem<T>(private val emptyValue: T) {
            var value = emptyValue
            var path: List<String> = emptyList()
            private val map = HashMap<String, NodeItem<T>>()

            fun childNode(name: String): NodeItem<T> = if (map.contains(name)) map[name] ?: TODO() else NodeItem(emptyValue).also { map[name] = it }

            fun build(
                parent: NodeOfTree.Index,
                allNodes: MutableList<NodeOfTree>,
                values: MutableList<T>,
                paths: MutableList<List<String>>,
                children: MutableList<NodeOfTree.Index>,
                block: (NodeOfTree, T) -> Unit,
            ) {
                val keys = map.keys.sorted()
                for (nodeName in keys) {
                    val child = map[nodeName] ?: TODO()
                    val nodeIndex = NodeOfTree.Index(allNodes.size)
                    val nodeChildren = mutableListOf<NodeOfTree.Index>()
                    val node = NodeOfTree(nodeName, NodeOfTree.Params(nodeIndex, parent, nodeChildren))
                    val nodeValue = child.value
                    allNodes.add(node)
                    values.add(nodeValue)
                    paths.add(child.path)
                    children.add(nodeIndex)
                    child.build(nodeIndex, allNodes, values, paths, nodeChildren, block)
                    block(node, nodeValue)
                }
            }
        }

        private val rootNodeTemp = NodeItem(emptyValue)

        private fun node(list: List<String>): NodeItem<T> {
            if (list.isEmpty()) TODO()
            var node = rootNodeTemp
            for (name in list) {
                node = node.childNode(name)
            }
            return node
        }

        fun put(path: List<String>, value: T): Unit = node(path).let {
            //println("NodeTreeBuilder.put $path")
            it.value = value
            it.path = path
        }

        fun build(block: (NodeOfTree, T) -> Unit = { _, _ -> }): Result<T> {
            val nodes = mutableListOf<NodeOfTree>()
            val values = mutableListOf<T>()
            val paths = mutableListOf<List<String>>()
            val children = mutableListOf<NodeOfTree.Index>()
            rootNodeTemp.build(NodeOfTree.Index(-1), nodes, values, paths, children, block)
            return Result(nodes, values, paths)
        }
    }
}
