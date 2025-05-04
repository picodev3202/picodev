class CharTransform(charsStrings: List<String>) {
    data class Result(val value: String, val updated: Boolean)

    val transformMaps by lazy {

        assert(charsStrings.size == 2) // TODO: support more chars sets

        val valueOfTransformMaps = Array(charsStrings.size) { mutableMapOf<Char, Char>() }
        val firstChars = charsStrings[0]
        val firstMap = valueOfTransformMaps[0]

        for (i in 1 until charsStrings.size) {
            val otherChars = charsStrings[i]
            val otherMap = valueOfTransformMaps[i]
            for (j in 0 until firstChars.length) {
                val firstChar = firstChars[j]
                val otherChar = otherChars[j]
                firstMap[firstChar] = otherChar
                otherMap[otherChar] = firstChar
            }
        }
        valueOfTransformMaps
    }

    @Suppress("unused") // ... to: Int ... support more chars sets
    fun transform(str: String, from: Int, to: Int): Result = transform(str, transformMaps[from])

    private fun transform(str: String, transformMap: MutableMap<Char, Char>): Result {
        val result = StringBuilder()
        for (fromChar in str) {
            val toChar = transformMap[fromChar]
            if (null == toChar) {
                // println("transform char for :$fromChar not found")
                return Result(str, false)
            }
            result.append(toChar)
        }
        return Result(result.toString(), true)
    }
}