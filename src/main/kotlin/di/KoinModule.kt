package di

import config.ConfigurationHelper
import org.koin.dsl.module
import scraper.Scraper
import service.ShowService
import service.ShowServiceImpl
import service.database.MyDatabase
import telegram.MyTelegramBot

val configuration = ConfigurationHelper.get()
val koinModule = module {
    single { ConfigurationHelper.get() }
    single { MyDatabase(configuration.database) }
    single { Scraper() }

    /* Services */
    single<ShowService> { ShowServiceImpl(get(), get()) }

    /* Telegram */
    single { MyTelegramBot(configuration.telegram, get()) }

}