package service

import model.Episode
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import scraper.Scraper
import service.database.MyDatabase
import service.database.model.EpisodeTable

interface EpisodeService {
    fun episodeExists(episodeTitle: String): Boolean
    fun findNewEpisodes(): List<Episode>
    fun addEpisode(episode: Episode)
    fun addEpisodeList(episodeList: Iterable<Episode>)
}

class EpisodeServiceImpl(private val myDb: MyDatabase, private val scraper: Scraper) : EpisodeService {
    override fun episodeExists(episodeTitle: String): Boolean =
        transaction(myDb.get()) {
            EpisodeTable.select { EpisodeTable.episodeName eq episodeTitle }.count() > 0
        }

    override fun findNewEpisodes(): List<Episode> =
        transaction(myDb.get()) {
            scraper.getTodayEpisodes()
                .filter { EpisodeTable.select { EpisodeTable.episodeName eq it.episodeName }.count() == 0 }
        }

    override fun addEpisode(episode: Episode) {
        transaction(myDb.get()) {
            EpisodeTable.insert {
                it[episodeName] = episode.episodeName
                it[url] = episode.url
                it[showName] = episode.showName
            }
        }
    }

    override fun addEpisodeList(episodeList: Iterable<Episode>) {
        transaction(myDb.get()) {
            EpisodeTable.batchInsert(episodeList, ignore = true) { episode: Episode ->
                this[EpisodeTable.showName] = episode.showName
                this[EpisodeTable.episodeName] = episode.episodeName
                this[EpisodeTable.url] = episode.url
            }
        }
    }
}