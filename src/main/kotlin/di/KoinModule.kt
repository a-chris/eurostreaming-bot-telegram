package di

import config.ConfigurationHelper
import org.koin.dsl.module
import scraper.Scraper
import service.*
import service.database.MyDatabase
import telegram.MyTelegramBot

val configuration = ConfigurationHelper.get()
val koinModule = module {
    single { configuration }
    single { MyDatabase(configuration.database) }
    single { Scraper(configuration.siteUrl) }

    /* Services */
    single<UserService> { UserServiceImpl(get()) }
    single<ShowService> { ShowServiceImpl(get(), get()) }
    single<EpisodeService> { EpisodeServiceImpl(get(), get()) }
    single<UserShowService> { UserShowServiceImpl(get()) }

    /* Telegram */
    single { MyTelegramBot(configuration.telegram) }

}