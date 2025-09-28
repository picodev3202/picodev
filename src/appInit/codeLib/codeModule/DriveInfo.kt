@Suppress("RemoveRedundantQualifierName")
object DriveInfo : ModuleHolder() {
    class Module() : To.Kt({
        of depends on(
            srcOf.main_object,
            srcOf.local_user_home,
            library.jackson_databind,
        )
    })
}