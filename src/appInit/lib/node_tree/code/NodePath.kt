class NodePath(val list: List<String>) {
    val fs by lazy { list.joinToString("/") }
    override fun toString() = fs
}