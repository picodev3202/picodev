object QuickNamedStringValue {
    operator fun getValue(@Suppress("unused") thisRef: Any?, property: kotlin.reflect.KProperty<*>) = property.name
}