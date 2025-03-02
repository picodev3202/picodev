// do not edit, file is generated
@Suppress("PropertyName")
class ParamsForUpdateValues {
    val highlight by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc1 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc2 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlights by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val highlights1 by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val highlights2 by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val highlights3 by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val localPathToCurrentFile by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_pathForLookupIdeScriptingPlace by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_prefixOfTmpDirBig by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_prefixOfTmpDirQuick by LocalPropertiesQuickHelperTool.QuickNamedString()
    val plus1 by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val plus1named by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val src by LocalPropertiesQuickHelperTool.QuickNamedHighlight()
    val user by LocalPropertiesQuickHelperTool.QuickNamedHighlight()

    private val map = mapOf(
        "localPathToCurrentFile" to localPathToCurrentFile,
        "highlight" to highlight,
        "highlights" to highlights,
        "highlights1" to highlights1,
        "highlights2" to highlights2,
        "highlights3" to highlights3,
        "plus1" to plus1,
        "plus1named" to plus1named,
        "src" to src,
        "highlightSrc" to highlightSrc,
        "highlightSrc1" to highlightSrc1,
        "highlightSrc2" to highlightSrc2,
        "user" to user,
        "params_prefixOfTmpDirBig" to params_prefixOfTmpDirBig,
        "params_prefixOfTmpDirQuick" to params_prefixOfTmpDirQuick,
        "params_pathForLookupIdeScriptingPlace" to params_pathForLookupIdeScriptingPlace,
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: LocalPropertiesQuickHelperTool.QuickNamedString.Val().apply { name = propertyName }

    fun forEach(action: (LocalPropertiesQuickHelperTool.QuickNamed.ValCommon) -> Unit): Unit = map.values.forEach(action)
}