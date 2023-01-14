@Suppress("PropertyName")
class ParamsForUpdateLocalProperties {
    val highlightSrc by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc1 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val highlightSrc2 by LocalPropertiesQuickHelperTool.QuickNamedString()
    val localPathToCurrentFile by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_pathForLookupIdeScriptingPlace by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_prefixOfTmpDirBig by LocalPropertiesQuickHelperTool.QuickNamedString()
    val params_prefixOfTmpDirQuick by LocalPropertiesQuickHelperTool.QuickNamedString()

    private val map = mapOf(
        "localPathToCurrentFile" to localPathToCurrentFile,
        "highlightSrc" to highlightSrc,
        "highlightSrc1" to highlightSrc1,
        "highlightSrc2" to highlightSrc2,
        "params_prefixOfTmpDirBig" to params_prefixOfTmpDirBig,
        "params_prefixOfTmpDirQuick" to params_prefixOfTmpDirQuick,
        "params_pathForLookupIdeScriptingPlace" to params_pathForLookupIdeScriptingPlace,
    )

    fun propertyByName(propertyName: String) = map[propertyName]
        ?: LocalPropertiesQuickHelperTool.QuickNamedString.Val().apply { name = propertyName; value = "" }
}