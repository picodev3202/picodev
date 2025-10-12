import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object CommonMark {

    fun htmlFromMd(mdText: String, raw: Boolean = false): String {
        val parser = Parser.builder().build()
        val document = parser.parse(mdText)
        val renderer = HtmlRenderer.builder().build()
        val html = renderer.render(document)
        return if (raw) html else """ 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

$html"""
    }

}
