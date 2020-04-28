package wikiapi

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*


fun wikiapi(name: String): Hashtable<String, Any>? {
    name.replace(" ", "_")
    val url = "https://en.wikipedia.org/wiki/" + name
    try {
        val document: Document = Jsoup.connect(url).get()
        val res =
            Hashtable<String, Any>()
        // Descrierea
        val desc: Element = document.select("table+p").get(0)
        val clade: Elements = document.select("table").get(0).getElementsByTag("tr")
        res["description"] = desc.text()
        val table =
            LinkedList<Pair<String, String>>()
        for (nume in clade) {
            if (nume.childNodeSize() == 4) {
                val key: String = nume.text().split(": ").get(0)
                val value: String = nume.text().split(": ").get(1)
                table.add(Pair(key, value))
            }
        }
        res["table"] = table
        return res
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
