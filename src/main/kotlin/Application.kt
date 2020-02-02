import di.koinModule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import scraper.Scraper
import telegram.MyTelegramBot

class App : KoinComponent {
    val scraper: Scraper by inject()
    val telegramBot: MyTelegramBot by inject()
}

fun main(args: Array<String>?) {
    startKoin {
        modules(koinModule)
    }

    with(App()) {
        scraper.showExists("Vikings")
        telegramBot.start()
    }
}