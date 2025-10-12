object CodeRunExample : DevObject() {

    /*@formatter:off*/ object Go001 {@JvmStatic fun main(args: Array<String>) = code01( ) }
    /*@formatter:off*/ object Go002 {@JvmStatic fun main(args: Array<String>) = code02( ) }
    //@formatter:on

    private fun code01() {
        println("$logTag.code01")
        println("$logTag.code01 $logTagName")
        println("$logTag.code01 $objectName")
    }

    private fun code02() {
        println("$logTag.code02")
        println("$logTag.code02 $logTag")
        println("$logTag.code02 $thisFile")
    }
}