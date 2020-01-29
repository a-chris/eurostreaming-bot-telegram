import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import scraper.Scraper

fun main() {
//    val site = "https://eurostreaming.pink/aggiornamento-episodi/"
//    val document = Jsoup.connect(site).get()
//    val list = mutableListOf<Element>()
//    val elements = document.body().selectFirst("div.entry").children()
//    var hasH4 = false
//    for (e in elements) {
//        if (e.`is`("h4")) {
//            if (hasH4) break
//            else {
//                list.add(e)
//                hasH4 = true
//            }
//        } else if (e.`is`("span")) {
//            list.add(e)
//        }
//    }
    val list = Scraper().getTodayEpisodes()
    println(list)
}