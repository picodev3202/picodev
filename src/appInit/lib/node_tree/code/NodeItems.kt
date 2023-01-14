abstract class NodeItems {
    abstract class SrcPlace : NodeItemsDesc.Node.SrcPlace by DefaultSrcPlace

    object DefaultSrcPlace : NodeItemsDesc.Node.SrcPlace {
        override val contentSrcPlace: NodeItemsDesc.Content
            get() = TODO("Not yet implemented")
    }

    data class ContentOfItem(
        override val type: NodeItemsDesc.Content.Type,
        override val dependency: List<NodeItemsDesc.ItemContent>,
        override val nodeFrom: Any,
        override val contentOf: NodeItemsDesc.ItemContent?,
        override val isReallyCopy: Boolean,
    ) : NodeItemsDesc.ItemContent

    abstract class NodeItemsDesc {
        interface Type {
            interface General : Content.Type
            interface Py : Content.Type
            interface Jv : Content.Type
            interface Kt : Content.Type
            interface KtJv : Content.Type
        }

        // @formatter:off
        interface Content {
            interface Type{ object Empty:Type }
            val type: Type
        }
        interface CopyOf {
            val contentOf: Content?
            val isReallyCopy :Boolean
        }
        interface ItemContent : Content , CopyOf {
            val nodeFrom:Any
            val dependency: List<ItemContent>
            override val contentOf: ItemContent?
        }
        /*sealed*/ interface Node {
            object Unit    :Node
            interface SrcPlace:Node  {    val contentSrcPlace: Content    }
            /*sealed*/ interface Item :Node{
                interface General :Item     {   val contentGeneral: ItemContent  }
                interface Py   :Item     {      val contentPy: ItemContent  }
                interface Jv   :Item     {      val contentJv: ItemContent  }
                interface Kt   :Item     {      val contentKt: ItemContent  }
            }
        }

        // @formatter:on

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

    open class ModulePath(list: List<String>, type: Type = default) : NodePath(list, type) {
        object Empty : ModulePath(emptyList(), emptyInstanceType)

        val ide by lazy { list.joinToString(".") }
    }

    open class NodePath(val list: List<String>, type: Type = default) : IsInstanceOfEmpty.Base(type) {
        object Empty : NodePath(emptyList(), emptyInstanceType)

        val fs by lazy { list.joinToString("/") }
        override fun toString() = fs
    }

    interface IsInstanceOfEmpty {
        abstract class Base(override val type: Type = defaultInstanceType) : IsInstanceOfEmpty {
            //object Empty : Base(emptyInstanceType)
            interface Type {
                object Empty : Type

                object Default : Type
            }

            companion object {
                val defaultInstanceType = Type.Default
                val default = defaultInstanceType
                val emptyInstanceType = Type.Empty
            }
        }

        val type: Base.Type
        val isInstanceOfEmpty /*   */ get() = type == Base.Type.Empty
        val isInstanceOfNotEmpty /**/ get() = type != Base.Type.Empty
        val isOE get() = isInstanceOfEmpty
        val isNE get() = isInstanceOfNotEmpty
    }

    open class ParamsBuilder<O, T>(val current: O) {
        val of: ParamsBuilder<O, T> get() = this
        operator fun invoke(type: T): ParamsBuilder<O, T> = this.apply {
            if (null == type) TODO("'type' can not be null}")
            _type = type
        }

        var _type: T? = null
        val _dependency = mutableListOf<O>()
        var _copyContentOf: O? = null
        var _isReallyCopy = false

        infix fun ParamsBuilder<O, T>.d(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>.dep(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>.depends(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>._depends(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>.__depends(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>.___depends(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>.____depends(list: Array<out O>) = _dependsOn(list)
        infix fun ParamsBuilder<O, T>._____depends(list: Array<out O>) = _dependsOn(list)


        fun on(vararg dependsOn: O): Array<out O> = dependsOn
        private fun _dependsOn(dependsOn: Array<out O>) {
            for (d in dependsOn) {
                if (null == d) TODO("dependency can not be null , '${dependsOn.toList()}'")
                if (current == d) TODO("dependency can not be the same: '$current' '$d'")
            }
            _dependency.addAll(dependsOn)
        }

        fun ParamsBuilder<O, T>.highlights() = Unit
        fun ParamsBuilder<O, T>.highlights() = Unit
        fun highlights() = Unit
        val highlights get() = Unit
        val ParamsBuilder<O, T>.highlights5 get() = Unit

        fun ParamsBuilder<O, T>.cp(from: O, isReallyCopy: Boolean = true) = _copyContentOf(from, isReallyCopy)
        fun ParamsBuilder<O, T>.copy(from: O, isReallyCopy: Boolean = true) = _copyContentOf(from, isReallyCopy)
        fun ParamsBuilder<O, T>.copyContent(from: O, isReallyCopy: Boolean = true) = _copyContentOf(from, isReallyCopy)
        fun ParamsBuilder<O, T>.copyContentOf(from: O, isReallyCopy: Boolean = true) = _copyContentOf(from, isReallyCopy)
//            fun ParamsBuilder<O, T>.copyContentOfIfNotExists(from: O, isReallyCopy: Boolean = true) = _copyContentOf(from, isReallyCopy)

        private fun _copyContentOf(from: O, isReallyCopy: Boolean) {
            if (null == from) TODO("'copyContentFrom' can not be null")
            if (current == from) TODO("'copyContentFrom' can not be the same: '$current' '$from'")
            _copyContentOf = from
            _isReallyCopy = isReallyCopy
        }
    }

    open class _Node : NodeItemsDesc.Node

    open class _General : general.O({ }) // maybe '_Empty' or maybe '_Generic' ...
    open class __General(action: general.B.() -> Unit) : general.O(action)

    open class _____________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ____________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ___________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class __________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class _________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ________Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class _______Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ______Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class _____Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ____Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class ___Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class __Jv(action: jv.B.() -> Unit) : _Jv(action)
    open class _Jv(action: jv.B.() -> Unit) : jv.O(action)

    open class _____________Py(action: py.B.() -> Unit) : _Py(action)
    open class ____________Py(action: py.B.() -> Unit) : _Py(action)
    open class ___________Py(action: py.B.() -> Unit) : _Py(action)
    open class __________Py(action: py.B.() -> Unit) : _Py(action)
    open class _________Py(action: py.B.() -> Unit) : _Py(action)
    open class ________Py(action: py.B.() -> Unit) : _Py(action)
    open class _______Py(action: py.B.() -> Unit) : _Py(action)
    open class ______Py(action: py.B.() -> Unit) : _Py(action)
    open class _____Py(action: py.B.() -> Unit) : _Py(action)
    open class ____Py(action: py.B.() -> Unit) : _Py(action)
    open class ___Py(action: py.B.() -> Unit) : _Py(action)
    open class __Py(action: py.B.() -> Unit) : _Py(action)
    open class _Py(action: py.B.() -> Unit) : py.O(action)

    open class _____________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ____________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ___________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class __________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ________________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _______________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ______________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _____________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ____________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ___________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class __________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ________Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _______Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ______Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _____Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ____Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class ___Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class __Kt(action: kt.B.() -> Unit) : _Kt(action)
    open class _Kt(action: kt.B.() -> Unit) : kt.O(action)

    open class _____________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ____________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ___________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class __________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class _________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ________KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class _______KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ______KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class _____KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ____KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class ___KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class __KtJv(action: ktJv.B.() -> Unit) : _KtJv(action)
    open class _KtJv(action: ktJv.B.() -> Unit) : ktJv.O(action)

    object general {
        open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.General, NodeItemsDesc.Type.General>(current)
        open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.General {
            override val contentGeneral: ContentOfItem by lazy {
                val b = B(this); b.action(); ContentOfItem(
                b._type ?: DefaultTypes.data.General,
                b._dependency.map { it.contentGeneral },
                this,
                b._copyContentOf?.contentGeneral,
                b._isReallyCopy
            )
            }
        }
    }

    object jv {
        open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.Jv, NodeItemsDesc.Type.Jv>(current)
        open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.Jv {
            override val contentJv: ContentOfItem by lazy {
                val b = B(this); b.action(); ContentOfItem(
                b._type ?: DefaultTypes.data.Jv,
                b._dependency.map { it.contentJv },
                this,
                b._copyContentOf?.contentJv,
                b._isReallyCopy
            )
            }
        }
    }

    object py {
        open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.Py, NodeItemsDesc.Type.Py>(current)
        open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.Py {
            override val contentPy: ContentOfItem by lazy {
                val b = B(this); b.action(); ContentOfItem(
                b._type ?: DefaultTypes.data.Py,
                b._dependency.map { it.contentPy },
                this,
                b._copyContentOf?.contentPy,
                b._isReallyCopy
            )
            }
        }
    }

    object kt {
        open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.Kt, NodeItemsDesc.Type.Kt>(current)
        open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.Kt {
            override val contentKt: ContentOfItem by lazy {
                val b = B(this); b.action(); ContentOfItem(
                b._type ?: DefaultTypes.data.Kt,
                b._dependency.map { it.contentKt },
                this,
                b._copyContentOf?.contentKt,
                b._isReallyCopy
            )
            }
        }
    }

    object ktJv {
        open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.Kt, NodeItemsDesc.Type.KtJv>(current)
        open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.Kt {
            override val contentKt: ContentOfItem by lazy {
                val b = B(this); b.action(); ContentOfItem(
                b._type ?: DefaultTypes.data.Kt,
                b._dependency.map { it.contentKt },
                this,
                b._copyContentOf?.contentKt,
                b._isReallyCopy
            )
            }
        }
    }
}