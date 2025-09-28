import java.util.*

object GroupsExample {

    data class DriveFile(val name: String, val id: String)

    class Title(val name: String) {
        val type1 = LinkedList<DriveFile>()
        val type2 = LinkedList<DriveFile>()
    }

    class Group(val mdPath: String) {
        val titles = LinkedList<Title>()
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val groups = Group("").apply {
            titles.add(Title("Title 01").apply {
                type1.add(DriveFile("...", "..."))
                type1.add(DriveFile("...", "..."))
                type2.add(DriveFile("...", "..."))
            })
            titles.add(Title("Title 02").apply {
                type1.add(DriveFile("...", "..."))
                type2.add(DriveFile("...", "..."))
            })
            titles.add(Title("Title 03").apply {
                type1.add(DriveFile("...", "..."))
                type2.add(DriveFile("...", "..."))
                type2.add(DriveFile("...", "..."))
            })
        }

    }

}
