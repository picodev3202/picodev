@Suppress(
    "FunctionName",
    "PropertyName",
    "ClassName",
    "unused",
)
abstract class /*NodeItems*/ To {
    object type {
        val KtCode = DefaultTypes.data.KtCode
        val JvKt = DefaultTypes.data.JvKt
        val Kt = DefaultTypes.data.Kt
        val Jv = DefaultTypes.data.Jv
        val BoardCode = DefaultTypes.data.BoardCode
    }

    val ________________________________________l = LL
    val _______________________________________l = LL
    val ______________________________________l = LL
    val _____________________________________l = LL
    val ____________________________________l = LL
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
    // val l = LL

    object LL {
        operator fun <T> minus(item: T) = item
    }

    open class __Library(name: String) : _Library(name)
    open class Library(name: String) : _Library(name)
    open class _Library(name: String) : NodeItemsDesc.Node.Item.Jv, NodeItemsDesc.Node.Item.Kt {
        override val contentJv = LibraryContent(name, this)
        override val contentKt = contentJv
    }

    abstract class SrcPlace : NodeItemsDesc.Node.SrcPlace

    open class _Node : NodeItemsDesc.Node

    open class __General : _General()
    open class General : Internal.general.O({ })
    open class _General : Internal.general.O({ })
    open class _General_(action: Internal.general.B.() -> Unit) : Internal.general.O(action)

    open class __Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class Jv(action: Internal.jv.B.() -> Unit) : _Jv(action)
    open class _Jv(action: Internal.jv.B.() -> Unit) : Internal.jv.O(action)
    open class _Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv({ of depends dependsOn })
    open class __Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv___of_depends_on(*dependsOn)
    open class Jv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Jv) : _Jv___of_depends_on(*dependsOn)

    open class __Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class Py(action: Internal.py.B.() -> Unit) : _Py(action)
    open class _Py(action: Internal.py.B.() -> Unit) : Internal.py.O(action)
    open class _Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py({ of depends dependsOn })
    open class __Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py___of_depends_on(*dependsOn)
    open class Py___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Py) : _Py___of_depends_on(*dependsOn)

    open class __Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class Kt(action: Internal.kt.B.() -> Unit) : _Kt(action)
    open class _Kt(action: Internal.kt.B.() -> Unit) : Internal.kt.O(action)
    open class _Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt({ of depends dependsOn })
    open class __Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)
    open class Kt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _Kt___of_depends_on(*dependsOn)

    open class KtJv(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class JvKt(action: Internal.ktJv.B.() -> Unit) : _KtJv(action)
    open class _KtJv(action: Internal.ktJv.B.() -> Unit) : Internal.ktJv.O(action)
    open class _KtJv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv({ of depends dependsOn })
    open class KtJv___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv___of_depends_on(*dependsOn)
    open class JvKt___of_depends_on(vararg dependsOn: NodeItemsDesc.Node.Item.Kt) : _KtJv___of_depends_on(*dependsOn)

    abstract class Internal {

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

            @Suppress("UnusedReceiverParameter")
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
    }
}