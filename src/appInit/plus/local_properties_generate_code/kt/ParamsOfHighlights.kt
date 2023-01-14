@Suppress("MemberVisibilityCanBePrivate")
class ParamsOfHighlights {
    val highlightSrc by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc1 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc2 by LocalPropertiesQuickHelperTool.QuickNamedString()

    private val map = mapOf(
        "highlightSrc" to highlightSrc,
        "highlightSrc1" to highlightSrc1,
        "highlightSrc2" to highlightSrc2,
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: LocalPropertiesQuickHelperTool.QuickNamedString.Val().apply { name = propertyName; value = "" }

    fun forEach(action: (LocalPropertiesQuickHelperTool.QuickNamedString.Val) -> Unit): Unit = map.values.forEach(action)
}