import config.Configuration
import di.koinModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import scraper.Scraper
import service.EpisodeService
import service.ShowService
import service.UserShowService
import telegram.MyTelegramBot

object App : KoinComponent {
    val configuration: Configuration by inject()
    val scraper: Scraper by inject()
    val telegramBot: MyTelegramBot by inject()
    val showService: ShowService by inject()
    val episodeService: EpisodeService by inject()
    val userShowService: UserShowService by inject()
}

fun main(args: Array<String>?) {
    startKoin {
        modules(koinModule)
    }

    with(App) {
        telegramBot.start()
    }
    println("I'm listening!")
    loop()
}

private fun loop() {
    runBlocking {
        while (true) {
            println("Looping")
            val newEpisodes = App.episodeService.findNewEpisodes()
            newEpisodes.forEach { episode ->
                val usersToNotify = App.userShowService.getUsersFollowingShow(episode.showName)
                usersToNotify.forEach { user ->
                    App.telegramBot.sendMessage(user.id, "New episode of ${episode.episodeName} is online")
                    if (!App.episodeService.episodeExists(episode.episodeName)) {
                        App.episodeService.addEpisode(episode)
                    }
                }
            }
            delay(App.configuration.interval)
        }
    }
}