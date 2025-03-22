@Suppress(
    "FunctionName",
    "PropertyName",
    "ClassName",
    "unused",
    "RemoveRedundantQualifierName"
)
abstract class NodeItems {
    object type {
        val KtCode = DefaultTypes.data.KtCode
        val JvKt = DefaultTypes.data.JvKt
        val Kt = DefaultTypes.data.Kt
        val Jv = DefaultTypes.data.Jv
        val BoardCode = DefaultTypes.data.BoardCode
    }

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

    fun <T> _______________________l(item: T) = item
    fun <T> ______________________l(item: T) = item
    fun <T> _____________________l(item: T) = item
    fun <T> ____________________l(item: T) = item
    fun <T> ___________________l(item: T) = item
    fun <T> __________________l(item: T) = item
    fun <T> _________________l(item: T) = item
    fun <T> ________________l(item: T) = item
    fun <T> _______________l(item: T) = item
    fun <T> ______________l(item: T) = item
    fun <T> _____________l(item: T) = item
    fun <T> ____________l(item: T) = item
    fun <T> ___________l(item: T) = item
    fun <T> __________l(item: T) = item
    fun <T> _________l(item: T) = item
    fun <T> ________l(item: T) = item
    fun <T> _______l(item: T) = item
    fun <T> ______l(item: T) = item
    fun <T> _____l(item: T) = item
    fun <T> ____l(item: T) = item
    fun <T> ___l(item: T) = item
    fun <T> __l(item: T) = item
    fun <T> _l(item: T) = item
    fun <T> l(item: T) = item

    val ___________________________________l = LL
    val __________________________________l = LL
    val _________________________________l = LL
    val ________________________________l = LL
    val _______________________________l = LL
    val ______________________________l = LL
    val _____________________________l = LL
    val ____________________________l = LL
    val ___________________________l = LL
    val __________________________l = LL
    val _________________________l = LL
    val ________________________l = LL
    val _______________________l = LL
    val ______________________l = LL
    val _____________________l = LL
    val ____________________l = LL
    val ___________________l = LL
    val __________________l = LL
    val _________________l = LL
    val ________________l = LL
    val _______________l = LL
    val ______________l = LL
    val _____________l = LL
    val ____________l = LL
    val ___________l = LL
    val __________l = LL
    val _________l = LL
    val ________l = LL
    val _______l = LL
    val ______l = LL
    val _____l = LL
    val ____l = LL
    val ___l = LL
    val __l = LL
    val _l = LL
    val l = LL
    //val NodeItems._l = LL
    //val l_ = LL
    //val l__ = LL
    //val l___ = LL
    //val l____ = LL
    //val l_____ = LL
    //val l______ = LL
    //val l_______ = LL
    //val l________ = LL
    //val l_________ = LL
    //val l__________ = LL
    //val l___________ = LL
    //val l____________ = LL
    //val l_____________ = LL
    //val l______________ = LL
    //val l_______________ = LL
    //val l________________ = LL
    //val l_________________ = LL
    //val l__________________ = LL
    //val _l_ = LL
    //val _l__ = LL
    //val _l___ = LL
    //val _l____ = LL
    //val _l_____ = LL
    //val _l______ = LL
    //val _l_______ = LL
    //val _l________ = LL
    //val _l_________ = LL
    //val _l__________ = LL
    //val _l___________ = LL

    object LL {
        infix fun <T> of(item: T) = item
        infix fun <T> o(item: T) = item
        operator fun <T> minus(item: T) = item  // https://kotlinlang.org/spec/operator-overloading.html#operator-overloading
    }

    class LibraryContent(val name: String, override val nodeFrom: Any) : NodeItemsDesc.ItemContent {
        override val dependency: List<NodeItemsDesc.ItemContent> = emptyList()
        override val contentOf: NodeItemsDesc.ItemContent? = null
        override val type = NodeItemsDesc.Content.Type.Empty
        override val isReallyCopy: Boolean = false
        override val renameTo: String = ""
    }

    open class ________________Library(name: String) : _Library(name)
    open class _______________Library(name: String) : _Library(name)
    open class ______________Library(name: String) : _Library(name)
    open class _____________Library(name: String) : _Library(name)
    open class ___________Library(name: String) : _Library(name)
    open class __________Library(name: String) : _Library(name)
    open class _________Library(name: String) : _Library(name)
    open class ________Library(name: String) : _Library(name)
    open class _______Library(name: String) : _Library(name)
    open class ______Library(name: String) : _Library(name)
    open class _____Library(name: String) : _Library(name)
    open class ____Library(name: String) : _Library(name)
    open class ___Library(name: String) : _Library(name)
    open class __Library(name: String) : _Library(name)
    open class _Library(name: String) : NodeItemsDesc.Node.Item.Jv, NodeItemsDesc.Node.Item.Kt {
        override val contentJv = LibraryContent(name, this)
        override val contentKt = contentJv
    }

    abstract class SrcPlace : NodeItemsDesc.Node.SrcPlace

    open class _Node : NodeItemsDesc.Node

    open class _________________General : _General()
    open class ________________General : _General()
    open class _______________General : _General()
    open class ______________General : _General()
    open class _____________General : _General()
    open class ____________General : _General()
    open class ___________General : _General()
    open class __________General : _General()
    open class _________General : _General()
    open class ________General : _General()
    open class _______General : _General()
    open class ______General : _General()
    open class _____General : _General()
    open class ____General : _General()
    open class ___General : _General()
    open class __General : _General()
    open class _General : Internal.general.O({ }) // maybe '_Empty' or maybe '_Generic' ...
    open class _General_(action: Internal.general.B.() -> Unit) : Internal.general.O(action)

    open class _____________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ____________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ___________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class __________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class _________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ________Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class _______Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ______Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class _____Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ____Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class ___Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class __Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class _Jv(action: Internal.jv.B.() -> Unit) : Internal.jv.O(action)
    open class _Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv({ of depends dependsOn })
    open class __Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv___of_depends_on(*dependsOn)
    open class Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv___of_depends_on(*dependsOn)

    open class _____________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ____________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ___________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class __________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class _________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ________Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class _______Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ______Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class _____Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ____Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class ___Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class __Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class _Py(action: Internal.py.B.() -> Unit) : Internal.py.O(action)
    open class _Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py({ of depends dependsOn })
    open class __Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py___of_depends_on(*dependsOn)
    open class Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py___of_depends_on(*dependsOn)

    open class ____________________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ___________________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class __________________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _________________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ________________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _______________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ______________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _____________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ____________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ___________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class __________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ________________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _______________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ______________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _____________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ____________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ___________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class __________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ________________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _______________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ______________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _____________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ____________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ___________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class __________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ________Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _______Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ______Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _____Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ____Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class ___Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class __Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _Kt(action: Internal.kt.B.() -> Unit) : Internal.kt.O(action)

    open class ________Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class _______Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class ______Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class _____Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class ____Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class ___Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class __Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class _Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt({ of depends dependsOn })
    //fun of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) :Internal.kt.B.() -> Unit { }

    open class __________________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _________________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ________________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _______________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ______________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _____________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ____________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ___________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class __________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ________________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _______________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ______________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _____________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ____________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ___________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class __________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ________________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _______________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ______________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _____________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ____________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ___________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class __________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ________KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _______KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ______KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _____KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ____KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class ___KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class __KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class JvKt(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _KtJv(action: Internal.ktJv.B.() -> Unit) : Internal.ktJv.O(action)
    open class _KtJv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv({ of depends dependsOn })
    open class KtJv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv___of_depends_on(*dependsOn)
    open class JvKt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv___of_depends_on(*dependsOn)

    abstract class Internal : NodeItemsDesc() {

        open class ParamsBuilder<O, T>(private val current: O) {
            val of: ParamsBuilder<O, T> get() = this
            operator fun invoke(type: T): ParamsBuilder<O, T> = this.apply {
                if (null == type) TODO("'type' can not be null}")
                _type = type
            }

            var _type: T? = null
            val _dependency = mutableListOf<O>()
            var _copyContentOf: O? = null
            var _isReallyCopy = false
            var _renameTo: String? = null

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

            val ParamsBuilder<O, T>.highlights get() = Unit

            fun ParamsBuilder<O, T>.cp(from: O, isReallyCopy: Boolean = true) = _copyContentOf(this, from, isReallyCopy)
            fun ParamsBuilder<O, T>.copy(from: O, isReallyCopy: Boolean = true) = _copyContentOf(this, from, isReallyCopy)
            fun ParamsBuilder<O, T>.copyContent(from: O, isReallyCopy: Boolean = true) = _copyContentOf(this, from, isReallyCopy)
            fun ParamsBuilder<O, T>.copyContentOf(from: O, isReallyCopy: Boolean = true) = _copyContentOf(this, from, isReallyCopy)
            // fun ParamsBuilder<O, T>.copyContentOfIfNotExists(from: O, isReallyCopy: Boolean = true) = _copyContentOf(this, from, isReallyCopy)

            private fun _copyContentOf(paramsBuilder: ParamsBuilder<O, T>, from: O, isReallyCopy: Boolean): ParamsBuilder<O, T> {
                if (null == from) TODO("'copyContentFrom' can not be null")
                if (current == from) TODO("'copyContentFrom' can not be the same: '$current' '$from'")
                _copyContentOf = from
                _isReallyCopy = isReallyCopy
                return paramsBuilder
            }

            fun ParamsBuilder<O, T>.renameTo(newName: String): ParamsBuilder<O, T> {
                _renameTo = newName
                return this
            }
        }

        data class ContentOfItem(
            override val type: NodeItemsDesc.Content.Type,
            override val dependency: List<NodeItemsDesc.ItemContent>,
            override val nodeFrom: Any,
            override val contentOf: NodeItemsDesc.ItemContent?,
            override val isReallyCopy: Boolean,
            override val renameTo: String?,
        ) : NodeItemsDesc.ItemContent

        object general {
            open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.General, NodeItemsDesc.Type.General>(current)
            open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.General {
                override val contentGeneral: ContentOfItem by lazy {
                    val b = B(this); b.action(); ContentOfItem(
                    b._type ?: DefaultTypes.data.General,
                    b._dependency.map { it.contentGeneral },
                    this,
                    b._copyContentOf?.contentGeneral,
                    b._isReallyCopy,
                    b._renameTo,
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
                    b._isReallyCopy,
                    b._renameTo,
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
                    b._isReallyCopy,
                    b._renameTo,
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
                    b._isReallyCopy,
                    b._renameTo,
                )
                }
            }
        }

        object ktJv {
            open class B(current: O) : ParamsBuilder<NodeItemsDesc.Node.Item.Kt, NodeItemsDesc.Type.KtJv>(current)
            open class O(action: B.() -> Unit) : NodeItemsDesc.Node.Item.Kt /*, NodeItemsDesc.Node.Item.Jv */{
                override val contentKt: ContentOfItem by lazy {
                    val b = B(this); b.action(); ContentOfItem(
                    b._type ?: DefaultTypes.data.Kt,
                    b._dependency.map { it.contentKt },
                    this,
                    b._copyContentOf?.contentKt,
                    b._isReallyCopy,
                    b._renameTo,
                )
                }
                //override val contentJv: ContentOfItem by lazy {
                //    val b = B(this); b.action(); ContentOfItem(
                //    b._type ?: DefaultTypes.data.Jv,
                //    b._dependency.map { it.contentKt },
                //    this,
                //    b._copyContentOf?.contentKt,
                //    b._isReallyCopy,
                //    b._renameTo,
                //)
                //}
            }
        }
    }
}