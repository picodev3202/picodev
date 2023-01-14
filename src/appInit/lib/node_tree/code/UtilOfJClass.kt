class UtilOfJClass {
    companion object : Lib()
    abstract class Lib {
        val propInstance = "INSTANCE"

        @Suppress("NOTHING_TO_INLINE")
        inline fun instantiateObject(classToGo: Class<*>): Any? {
            if (classToGo.declaredFields.map { it.name }.contains(propInstance)) {
                @Suppress("UnnecessaryVariable") val objInstance = classToGo.getField(propInstance).get(null)
                return objInstance
            }
            return null
        }

        fun listOfClasses(javaClass: Class<*>?, block: (Class<*>) -> Unit) {
            javaClass?.run {
                block(this)
                declaredClasses.forEach { listOfClasses(it, block) }
            }
        }
    }
}