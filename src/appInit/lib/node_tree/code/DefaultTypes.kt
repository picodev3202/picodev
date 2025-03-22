object DefaultTypes {

    abstract class ItemType(
        val srcs: List<Pair<String, Pair<String, (String, String) -> String>>>,
        val withExample: Boolean,
        val moduleXmlStr: (String, List<String>, List<String>) -> String
    ) : NodeItems.NodeItemsDesc.Content.Type

    @Suppress("ClassName")
    object data {
        val emptyExample = "" to fun(_: String, _: String) = ""

        val ktExample = "K.kt" to fun(name: String, msg: String) = """object $name {
    @JvmStatic
    fun main(args: Array<String>) {
        println("$msg")
    }
}"""

        val jvExample = "J.java" to fun(name: String, msg: String) = """public class $name {
    public static void main(String[] args) {
        System.out.println("$msg");
    }
}"""
        val moduleXmlKt = IdeModulesXml.moduleXmlKt
        val moduleXmlJv = IdeModulesXml.moduleXmlJv
        val moduleXmlBoardCode = IdeModulesXml.moduleXmlBoardCode
        val moduleXmlGeneral = IdeModulesXml.moduleXmlGeneral

        object General : ItemType(listOf(), false, moduleXmlGeneral), NodeItems.NodeItemsDesc.Type.General
        object KtCode : ItemType(listOf("code" to ktExample), false, moduleXmlKt), NodeItems.NodeItemsDesc.Type.Kt
        object JvKt : ItemType(listOf("kt" to ktExample, "jv" to jvExample), false, moduleXmlKt), NodeItems.NodeItemsDesc.Type.KtJv
        object Kt : ItemType(listOf("kt" to jvExample), false, moduleXmlKt), NodeItems.NodeItemsDesc.Type.Kt
        object Jv : ItemType(listOf("jv" to jvExample), false, moduleXmlJv), NodeItems.NodeItemsDesc.Type.Jv
        object Py : ItemType(listOf("py" to emptyExample), false, moduleXmlBoardCode), NodeItems.NodeItemsDesc.Type.Py
        object BoardCode : ItemType(
            listOf(
                "py         " to emptyExample,
                "py_local   " to emptyExample,
                "lib_sources" to emptyExample,
                "lib_local  " to emptyExample,
                "lib_py     " to emptyExample,
            ), false, moduleXmlBoardCode
        ), NodeItems.NodeItemsDesc.Type.Py
    }
}
