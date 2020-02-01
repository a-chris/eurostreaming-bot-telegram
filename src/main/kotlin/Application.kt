import config.ConfigurationHelper
import scraper.Scraper
import service.database.MyDatabase
import telegram.MyTelegramBot

fun main() {
    val configuration = ConfigurationHelper.get()
//    val list = Scraper().getTodayEpisodes()
//    MyDatabase.test()


    Scraper().showExists("Vikings")


//    val myBd = MyDatabase(configuration.database)
//    MyTelegramBot(configuration.telegram, myBd).start()
}