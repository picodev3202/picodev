@Suppress("unused")
abstract class ModuleHolder() : To() {
    val library get() = appInit.library
    val srcOf get() = appInit.srcOf
}