package service

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import scraper.Scraper
import service.database.MyDatabase
import service.database.model.ShowTable

interface ShowService {
    fun showExists(showName: String): Boolean

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
                addShow(showName)
            }
            resultFromScraper
        }
    }

    private fun addShow(showName: String) {
        transaction(db.get()) {
            ShowTable.insert {
                it[name] = showName
            }
        }
    }

}