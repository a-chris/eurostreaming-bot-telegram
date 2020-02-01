package di

import config.ConfigurationHelper
import org.koin.dsl.module
import scraper.Scraper
import service.ShowServiceImpl
import service.database.MyDatabase
import telegram.MyTelegramBot

val koinModule = module {
    single { ConfigurationHelper.get() }
    single { MyDatabase(get()) }
    single { Scraper() }
    single { MyTelegramBot(get(), get()) }
    /* Services */
    single { ShowServiceImpl(get(), get()) }
}