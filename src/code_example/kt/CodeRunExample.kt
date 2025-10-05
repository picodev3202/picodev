object CodeRunExample {
    val logTag by lazy { Val.detectLogTag() }
    val thisFile by lazy { Val.lookupSrcFileByClassName() }

    object Val : MainObject() {
        val devProject by lazy { DevProjectLookup.fromCurrentDir() }
    }

    /*@formatter:off*/ object Go001 {@JvmStatic fun main(args: Array<String>) = code01( ) }
    /*@formatter:off*/ object Go002 {@JvmStatic fun main(args: Array<String>) = code02( ) }
    //@formatter:on

    private fun code01() {
        println("$logTag.code01")
        println("$logTag.code01 ${Val.devProject}")
        println("$logTag.code01 ${Val.logTagName}")
        println("$logTag.code01 ${Val.objectName}")
    }

    private fun code02() {
        println("$logTag.code02")
        println("$logTag.code02 $logTag")
        println("$logTag.code02 $thisFile")
    }
}