package service

import model.Show
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import scraper.Scraper
import service.database.MyDatabase
import service.database.model.ShowTable

interface ShowService {
    fun showExists(showName: String): Boolean
    fun findSimilarShows(showName: String): List<Show>
    fun addShow(show: Show)
}

class ShowServiceImpl(private val db: MyDatabase, private val scraper: Scraper) : ShowService {

    override fun showExists(showName: String): Boolean {
        val resultFromDatabase = transaction(db.get()) {
            ShowTable.select { ShowTable.name eq showName }.count() > 0
        }
        return if (resultFromDatabase) {
            resultFromDatabase
        } else {
            val resultFromScraper = scraper.showExists(showName)
            if (resultFromScraper) {
                addShow(Show(showName))
            }
            resultFromScraper
        }
    }

    override fun findSimilarShows(showName: String): List<Show> =
        transaction(db.get()) {
            ShowTable
                .select { ShowTable.name like "%${showName}%" }
                .map { Show(it[ShowTable.name]) }
        }

    override fun addShow(show: Show) {
        transaction(db.get()) {
            ShowTable.insert {
                it[name] = show.name
            }
        }
    }

}