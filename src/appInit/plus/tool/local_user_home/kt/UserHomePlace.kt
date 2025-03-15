object UserHomePlace : LocalPlace.Abstract() {
    override val file by lazy { LocalPlace.localFile(System.getProperty("user.home")).let { if (it.exists() && it.isDirectory) it else TODO() } }
}