@Suppress("RemoveRedundantQualifierName", "unused", "MoveLambdaOutsideParentheses", "PropertyName")
object CommonLib : ModuleHolder() {
    class Module() : To.General() {

        val object_mapper = ___l - Kt({ of depends on(library.jackson_databind) })

        val common_mark = _____l - Kt({ of depends on(library.common_mark) })


    }
}