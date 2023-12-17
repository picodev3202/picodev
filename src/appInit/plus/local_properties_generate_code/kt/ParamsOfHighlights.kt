@Suppress("MemberVisibilityCanBePrivate")
class ParamsOfHighlights {
    val highlight by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val highlightSrc by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc1 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc2 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc3 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val plus1 by LocalPropertiesQuickHelperTool.QuickNamedHighlight()

    private val map = mapOf(
        "highlight" to highlight,
        "plus1" to plus1,
        "highlightSrc" to highlightSrc,
        "highlightSrc1" to highlightSrc1,
        "highlightSrc2" to highlightSrc2,
        "highlightSrc3" to highlightSrc3,
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: LocalPropertiesQuickHelperTool.QuickNamedString.Val().apply { name = propertyName }

    fun forEach(action: (LocalPropertiesQuickHelperTool.QuickNamed.ValCommon) -> Unit): Unit = map.values.forEach(action)
}