package wikiapi

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

fun wikiapi(name: String): Hashtable<String, String>? {
    name.replace(" ", "_")
    val url = "https://en.wikipedia.org/wiki/$name"
    try {
        println("beforeJSOUP")
        val document: Document = Jsoup.connect(url).get()
        println("afterJSOUP")

        val res = Hashtable<String, String>()
        // Descrierea
        val desc: Element = document.select("table+p")[0]
        val clade: Elements = document.select("table")[0].getElementsByTag("tr")
        res["description"] = desc.text()
        var table = ""
        for (nume in clade) {
            if (nume.childNodeSize() == 4) {
                val key: String = nume.text().split(": ")[0]
                val value: String = nume.text().split(": ")[1]
                table="$table$key,$value,"
            }
        }
        res["table"] = table
        return res
    } catch (e: Exception) {
        println("----------------------")
        e.printStackTrace()
        println("----------------------")
        return null
    }
}
