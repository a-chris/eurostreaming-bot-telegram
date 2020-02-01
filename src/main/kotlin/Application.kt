import di.koinModule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import scraper.Scraper

class App : KoinComponent {
    val scraper: Scraper by inject()
}

fun main(args: Array<String>?) {
    startKoin {
        modules(koinModule)
    }

//    val list = Scraper().getTodayEpisodes()
//    MyDatabase.test()


    App().scraper.showExists("Vikings")


//    val myBd = MyDatabase(configuration.database)
//    MyTelegramBot(configuration.telegram, myBd).start()
}