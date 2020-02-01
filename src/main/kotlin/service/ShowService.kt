package service

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import service.database.MyDatabase
import service.database.model.Show

interface ShowService {
    fun showExists(showName: String): Boolean
}

class ShowServiceImpl(private val db: MyDatabase) : ShowService {

    override fun showExists(showName: String): Boolean {
        return transaction(db.get()) {
            Show.select { Show.name eq showName }.count() > 0
        }
    }

}