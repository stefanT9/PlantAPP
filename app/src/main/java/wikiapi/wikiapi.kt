package wikiapi

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import plantToTextAPI.OnTaskEventListener
import java.util.*


class WikiapiTask(callback: OnTaskEventListener<Hashtable<String, String>>) : AsyncTask<String?, Int?, Hashtable<String, String>?>() {
    private var mCallBack: OnTaskEventListener<Hashtable<String, String>> = callback

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("Wikiapi", "Wikiapi started")
    }

    override fun doInBackground(vararg params: String?): Hashtable<String, String>? {
        //Get bitmap from parameters
        val plantName = params[0]
        return wikiapi(plantName)
    }

    override fun onPostExecute(result: Hashtable<String, String>?) {
        super.onPostExecute(result);
        if (result != null) {
            mCallBack?.onSuccess(result);
        } else {
            mCallBack?.onFailure(java.lang.Exception("Wikiapi failed"));
        }
        Log.d("Wikiapi", "Wikiapi finished!")
    }
}

fun wikiapi(name: String? ): Hashtable<String, String>? {
    if(name==null)
        return null

    name.replace(" ", "_")
    val url = "https://en.wikipedia.org/wiki/$name"
    try {
        val document: Document = Jsoup.connect(url).get()

        val res = Hashtable<String, String>()
        val photoUrl = document.selectFirst(".image img")?.absUrl("src")
        println(photoUrl)

        val desc: Element = document.select("table+p")[0]
        val clade: Elements = document.select("table")[0].getElementsByTag("tr")
        res["description"] = desc.text()
        var table = ""
        for (nume in clade) {
            if (nume.childNodeSize() == 4) {
                val key: String = nume.text().split(": ")[0]
                val value: String = nume.text().split(": ")[1]
                table = if(table.isNotEmpty())
                    "$table,$key,$value"
                else
                    "$key,$value"
            }
        }
        res["table"] = table
        res["image"] = photoUrl
        return res
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
