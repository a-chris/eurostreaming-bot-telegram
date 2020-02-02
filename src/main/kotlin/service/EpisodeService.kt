package service

import model.Episode
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import scraper.Scraper
import service.database.MyDatabase
import service.database.model.EpisodeTable

interface EpisodeService {
    fun findNewEpisodes(): List<Episode>
    fun addEpisode(episode: Episode)
}

class EpisodeServiceImpl(private val db: MyDatabase, private val scraper: Scraper) : EpisodeService {
    override fun findNewEpisodes(): List<Episode> =
        transaction(db.get()) {
            scraper.getTodayEpisodes()
                .filter { EpisodeTable.select { EpisodeTable.name eq it.showName }.count() == 0 }
        }

    override fun addEpisode(episode: Episode) {
        transaction(db.get()) {
            EpisodeTable.insert {
                it[name] = episode.title
                it[url] = episode.url
                it[show] = episode.showName
            }
        }
    }
}