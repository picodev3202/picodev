class QuickNamedString {
    class Val {
        var name = ""
        var value = ""
        operator fun rangeTo(value: String): Unit = run { this.value = value }
        override fun toString() = "(name=$name, value=$value)"
    }

    private val _val = Val()
    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>) = _val.apply { name = property.name }
}