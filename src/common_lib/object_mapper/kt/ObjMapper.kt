import com.fasterxml.jackson.databind.ObjectMapper

object ObjMapper {

    val instance by lazy { instance() }

    fun instance() = ObjectMapper()

    inline fun <reified T> readValue(str: String) = instance.readValue(str, T::class.java)

}
