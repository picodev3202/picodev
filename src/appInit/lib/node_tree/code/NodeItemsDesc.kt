abstract class NodeItemsDesc {
    interface Type {
        interface General : Content.Type
        interface Py : Content.Type
        interface Jv : Content.Type
        interface Kt : Content.Type
        interface KtJv : Content.Type
    }

    //@formatter:off
        interface Content {
            interface Type{ object Empty:Type }
            val type: Type
        }
        interface CopyOf {
            val contentOf: Content?
            val isReallyCopy: Boolean
        }
        interface RenameTo {
            val renameTo: String?
        }
        interface ItemContent : Content, CopyOf, RenameTo {
            val nodeFrom: Any
            val dependency: List<ItemContent>
            override val contentOf: ItemContent?
        }
        /*sealed*/ interface Node {
            object Unit    :Node
            interface SrcPlace:Node
            /*sealed*/ interface Item :Node{
                interface General :Item     {   val contentGeneral: ItemContent  }
                interface Py   :Item     {      val contentPy: ItemContent  }
                interface Jv   :Item     {      val contentJv: ItemContent  }
                interface Kt   :Item     {      val contentKt: ItemContent  }
            }
        }

        //@formatter:on

//        fun readType(n: Node) = content(n).type
//        fun content(n: Node): Content {
//            return when (n) {
//                is Node.SrcPlace -> n.contentSrcPlace
//                is Node.Item -> when (n) {
//                    is Node.Item.Kt -> n.contentKt
//                    is Node.Item.Py -> n.contentPy
//                    is Node.Item.Jv -> n.contentJv
//                    else -> TODO()
//                }
//
//                else -> TODO()
//            }
//        }

    fun itemContent(n: Node.Item): ItemContent {
        return when (n) {
            is Node.Item.General -> n.contentGeneral
            is Node.Item.Kt -> n.contentKt
            is Node.Item.Jv -> n.contentJv
            is Node.Item.Py -> n.contentPy
            else -> TODO()
        }
    }
}